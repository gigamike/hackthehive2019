<?php

namespace User\Service;

use Zend\Mvc\Controller\AbstractActionController;

use User\Model\UserLocationEntity;

use GeoIp2\Database\Reader;

class ServiceLastLogin extends AbstractActionController
{
  public function getUserMapper()
  {
    $sm = $this->getServiceLocator();
    return $sm->get('UserMapper');
  }

  public function getUserLocationMapper()
  {
    $sm = $this->getServiceLocator();
    return $sm->get('UserLocationMapper');
  }

  public function setLastLogin(){
    $authService = $this->serviceLocator->get('auth_service');
    if($authService->hasIdentity()) {
      $user = $this->getUserMapper()->getUser($authService->getIdentity()->id);
      if($user){
        $config = $this->getServiceLocator()->get('Config');
        if (isset($_SERVER["HTTP_CF_CONNECTING_IP"])) {
          $_SERVER['REMOTE_ADDR'] = $_SERVER["HTTP_CF_CONNECTING_IP"];
        }
        if($_SERVER['REMOTE_ADDR'] == '127.0.0.1'){
          $_SERVER['REMOTE_ADDR'] = $config['ip'];
        }

        if($_SERVER['REMOTE_ADDR']){
          $countryCode = null;
          $country = null;
          $city = null;
          $latitude = 0;
          $longitude = 0;

          $basePath = dirname($_SERVER['DOCUMENT_ROOT']);
          $reader = new Reader($basePath . "/geoip/GeoLite2-City.mmdb");

          $record = $reader->city($_SERVER['REMOTE_ADDR']);

          $latitude = $record->location->latitude;
          $longitude = $record->location->longitude;
          $countryCode = $record->country->isoCode;
          $city = $record->city->name;
          $country = $record->country->name;

          /*
          echo "Your IP: " . $_SERVER['REMOTE_ADDR'] . "<br>";
          echo "Your Country Code: " . $record->country->isoCode . "<br>";
          echo "Your Country Name: " . $record->country->name . "<br>";
          echo "Your latitude: " . $record->location->latitude . "<br>";
          echo "Your longitude " . $record->location->longitude . "<br>";
          echo "Your City Name: " . $record->city->name . "<br>";
          */
        }

        $userLocation = $this->getUserLocationMapper()->getUserLocationByUserId($user->getId());
        if($userLocation){
          $userLocation->setIp($_SERVER['REMOTE_ADDR']);
          $userLocation->setCountryCode($countryCode);
          $userLocation->setCountryName($country);
          $userLocation->setCity($city);
          $userLocation->setLatitude($latitude);
          $userLocation->setLongitude($longitude);
          $this->getUserLocationMapper()->saveUserLocation($userLocation);

          $user->setLastLoginDatetime(date('Y-m-d H:i:s'));
          $this->getUserMapper()->saveUser($user);
        }else{
          $userLocation = new UserLocationEntity();
          $userLocation->setUserId($user->getId());
          $userLocation->setIp($_SERVER['REMOTE_ADDR']);
          $userLocation->setCountryCode($countryCode);
          $userLocation->setCountryName($country);
          $userLocation->setCity($city);
          $userLocation->setLatitude($latitude);
          $userLocation->setLongitude($longitude);
          $this->getUserLocationMapper()->saveUserLocation($userLocation);

          $user->setLastLoginDatetime(date('Y-m-d H:i:s'));
          $this->getUserMapper()->saveUser($user);
        }
      }
    }
  }
}

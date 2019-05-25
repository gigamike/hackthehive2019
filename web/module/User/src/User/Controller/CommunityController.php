<?php

namespace User\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\Mail\Message as Message;
use Zend\Mail\Transport\Sendmail as Sendmail;

use User\Model\UserEntity;
use User\Model\UserLocationEntity;

use GeoIp2\Database\Reader;
use Gumlet\ImageResize;

class CommunityController extends AbstractActionController
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

    public function getCountryMapper()
    {
      $sm = $this->getServiceLocator();
      return $sm->get('CountryMapper');
    }

    public function getOrganizationMapper()
    {
      $sm = $this->getServiceLocator();
      return $sm->get('OrganizationMapper');
    }

    public function indexAction()
    {
      $page = $this->params()->fromRoute('page') ? (int) $this->params()->fromRoute('page') : 1;
      $search_by = $this->params()->fromRoute('search_by') ? $this->params()->fromRoute('search_by') : '';

      $this->_getIP();

      $latitude = null;
      $longitude = null;
      $city = null;
      $countryCode = null;
      $country = null;
      if($_SERVER['REMOTE_ADDR']){
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

      $filter = array(
        'organization_id' => null,
        'country_code' => null,
        'city_keyword' => null,
      );
      if (!empty($search_by)) {
        $filter = (array) json_decode($search_by);
      }
      $filter['role'] = 'volunteer';
      $filter['latitude'] = $latitude;
      $filter['longitude'] = $longitude;

      $authService = $this->serviceLocator->get('auth_service');
      if ($authService->getIdentity()!=null) {
        $filter['id_not'] = $authService->getIdentity()->id;
      }

      $order = array(
        'distance',
        'first_name',
        'last_name',
      );
      $paginator = $this->getUserMapper()->getVolunteers(true, $filter, $order);
      $paginator->setCurrentPageNumber($page);
      $paginator->setItemCountPerPage(10);

      $organizations = $this->getOrganizationMapper()->fetchAll(false, array(), array('organization'));
      $countries = $this->getCountryMapper()->fetchAll(false, array(), array('country_name'));

      return new ViewModel(array(
        'page' => $page,
        'search_by' => $search_by,
        'paginator' => $paginator,
        'organizations' => $organizations,
        'currentCountry' => $country,
        'currentCity' => $city,
        'filter' => $filter,
        'countries' => $countries,
      ));
    }

    public function searchAction()
    {
      $request = $this->getRequest();
      if ($request->isPost()) {
        $formdata = (array) $request->getPost();

        $search_data = array();
        foreach ($formdata as $key => $value) {
          if ($key != 'submit') {
            $search_data[$key] = urlencode($value);
          }
        }

        if (!empty($search_data)) {
          $search_by = json_encode($search_data);

          return $this->redirect()->toRoute('community', array('action' => 'index', 'search_by' => $search_by));
        }else{
          return $this->redirect()->toRoute('community', array('action' => 'index',));
        }
      }else{
        return $this->redirect()->toRoute('community', array('action' => 'index',));
      }
    }

    public function ofwAction()
    {
      $config = $this->getServiceLocator()->get('Config');
      $this->_getIP();

      $form = $this->getServiceLocator()->get('RegistrationOfwForm');
      $user = new UserEntity();
      $form->bind($user);

      $latitude = null;
      $longitude = null;
      $city = null;
      $countryCode = null;
      $country = null;
      if($_SERVER['REMOTE_ADDR']){
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

      $request = $this->getRequest();
      if ($request->isPost()) {
        $form->setData($request->getPost()->toArray());
        if ($form->isValid()) {
          $email = $request->getPost('email');
          $first_name = $request->getPost('first_name');
          $middle_name = $request->getPost('middle_name');
          $last_name = $request->getPost('last_name');
          $password = $request->getPost('password');
          $country_id = $request->getPost('country_id');
          $city = $request->getPost('city');
          $mobile_no = $request->getPost('mobile_no');

          $role = 'ofw';

          $authService = $this->serviceLocator->get('auth_service');
          $isProfilePictureError = false;
          if(!isset($_FILES['profile_picture'])){
            $isProfilePictureError = true;
            $form->get('profile_picture')->setMessages(array('Required field Profile Picture.'));
          }else{
            $allowed =  array('png','gif','jpeg','jpg');
            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            if(!in_array($ext, $allowed) ) {
              $isProfilePictureError = true;
              $form->get('profile_picture')->setMessages(array("File type not allowed. Only " . implode(',', $allowed)));
            }
            switch ($_FILES['profile_picture']['error']){
              case 1:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('The file is bigger than this PHP installation allows.'));
                break;
              case 2:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('The file is bigger than this form allows.'));
                break;
              case 3:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('Only part of the file was uploaded.'));
                break;
              case 4:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('No file was uploaded.'));
                break;
              default:
            }
          }

          if(!$isProfilePictureError){
            $user->setRole($role);
            $user->setActive('Y');
            $user->setEmail($email);
            $user->setFirstName($first_name);
            $user->setMiddleName($middle_name);
            $user->setLastName($last_name);
            $user->setOrganizationId(0);

            $dynamicSalt = $this->getUserMapper()->dynamicSalt();
            $user->setSalt($dynamicSalt);
            $config = $this->getServiceLocator()->get('Config');
            $password = md5($config['staticSalt'] . $password . $dynamicSalt);
            $user->setPassword($password);

            $user->setProfilePic($_FILES['profile_picture']['name']);
            $user->setLastLoginDatetime(date('Y-m-d H:i:s'));
            $user->setCreatedUserId(0);
            $this->getUserMapper()->saveUser($user);

            $userLocation = new UserLocationEntity();
            $userLocation->setUserId($user->getId());
            $userLocation->setIp($_SERVER['REMOTE_ADDR']);
            $userLocation->setCountryCode($countryCode);
            $userLocation->setCountryName($country);
            $userLocation->setCity($city);
            $userLocation->setLatitude($latitude);
            $userLocation->setLongitude($longitude);
            $this->getUserLocationMapper()->saveUserLocation($userLocation);

            $directory = $config['pathImageUser']['absolutePath'] . $user->getId();
            if(!file_exists($directory)){
              mkdir($directory, 0755);
            }

            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            $destination = $directory . "/photo-orig." . $ext;
            if(!file_exists($destination)){
               move_uploaded_file($_FILES['profile_picture']['tmp_name'], $destination);
            }

            $destination2 = $directory . "/photo-resize." . $ext;
            if(file_exists($destination2)){
               unlink($destination2);
            }
            $image = new ImageResize($destination);
            $image->resizeToWidth(750, 450);
            $image->save($destination2);

            $destination3 = $directory . "/photo-750x450." . $ext;
            if(file_exists($destination3)){
               unlink($destination3);
            }
            $image = new ImageResize($destination2);
            $image->crop(750, 450, 0, 450);
            $image->save($destination3);

            $mail = new  Message();

            $subject = "Welcome";
            $message = "Hi " . $first_name . ",\n\n";
            $message .= "You successfully registered, pls. login on " . $config['baseUrl'] . "/login \n";
            $message .= "If in case you need help, you can check out the URL below.\n\n";
            $message .= "\nOr you can reach us through " . $config['baseUrl'] . "/contact to serve you.\n";
            $message .= "\n\nThank You,\n";
            $message .= "Admin :)";

            $mail->setFrom($config['email']);
            $mail->addTo($email);
            $mail->setEncoding("UTF-8");
            $mail->setSubject($subject);
            $mail->setBody($message);

            $transport = new Sendmail();
            // $transport->send($mail);

            $this->flashMessenger()->setNamespace('success')->addMessage($role . " successfully registered.");
            return $this->redirect()->toRoute('login');
          }
        }else{
          // print_r($form->getMessages());
        }
      }else{
        if(!empty($countryCode)){
          $country = $this->getCountryMapper()->getCountryCode($countryCode);
          if($country){
            $form->get('country_id')->setValue($country->getId());
          }
        }

        $form->get('city')->setValue($city);
      }

      return new ViewModel(array(
        'form' => $form,
      ));
    }

    public function volunteerAction()
    {
      $config = $this->getServiceLocator()->get('Config');
      $this->_getIP();

      $form = $this->getServiceLocator()->get('RegistrationVolunteerForm');
      $user = new UserEntity();
      $form->bind($user);

      $latitude = null;
      $longitude = null;
      $city = null;
      $countryCode = null;
      $country = null;
      if($_SERVER['REMOTE_ADDR']){
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

      $request = $this->getRequest();
      if ($request->isPost()) {
        $form->setData($request->getPost()->toArray());
        if ($form->isValid()) {
          $email = $request->getPost('email');
          $first_name = $request->getPost('first_name');
          $middle_name = $request->getPost('middle_name');
          $last_name = $request->getPost('last_name');
          $password = $request->getPost('password');
          $country_id = $request->getPost('country_id');
          $city = $request->getPost('city');
          $mobile_no = $request->getPost('mobile_no');
          $organization_id = $request->getPost('organization_id');

          $role = 'volunteer';

          $authService = $this->serviceLocator->get('auth_service');
          $isProfilePictureError = false;
          if(!isset($_FILES['profile_picture'])){
            $isProfilePictureError = true;
            $form->get('profile_picture')->setMessages(array('Required field Profile Picture.'));
          }else{
            $allowed =  array('png','gif','jpeg','jpg');
            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            if(!in_array($ext, $allowed) ) {
              $isProfilePictureError = true;
              $form->get('profile_picture')->setMessages(array("File type not allowed. Only " . implode(',', $allowed)));
            }
            switch ($_FILES['profile_picture']['error']){
              case 1:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('The file is bigger than this PHP installation allows.'));
                break;
              case 2:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('The file is bigger than this form allows.'));
                break;
              case 3:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('Only part of the file was uploaded.'));
                break;
              case 4:
                $isProfilePictureError = true;
                $form->get('profile_picture')->setMessages(array('No file was uploaded.'));
                break;
              default:
            }
          }

          if(!$isProfilePictureError){
            $user->setRole($role);
            $user->setActive('Y');
            $user->setEmail($email);
            $user->setFirstName($first_name);
            $user->setMiddleName($middle_name);
            $user->setLastName($last_name);
            $user->setOrganizationId($organization_id);

            $dynamicSalt = $this->getUserMapper()->dynamicSalt();
            $user->setSalt($dynamicSalt);
            $password = md5($config['staticSalt'] . $password . $dynamicSalt);
            $user->setPassword($password);

            $user->setProfilePic($_FILES['profile_picture']['name']);
            $user->setLastLoginDatetime(date('Y-m-d H:i:s'));
            $user->setCreatedUserId(0);
            $this->getUserMapper()->saveUser($user);

            $userLocation = new UserLocationEntity();
            $userLocation->setUserId($user->getId());
            $userLocation->setIp($_SERVER['REMOTE_ADDR']);
            $userLocation->setCountryCode($countryCode);
            $userLocation->setCountryName($country);
            $userLocation->setCity($city);
            $userLocation->setLatitude($latitude);
            $userLocation->setLongitude($longitude);
            $this->getUserLocationMapper()->saveUserLocation($userLocation);

            $directory = $config['pathImageUser']['absolutePath'] . $user->getId();
            if(!file_exists($directory)){
              mkdir($directory, 0755);
            }

            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            $destination = $directory . "/photo-orig." . $ext;
            if(!file_exists($destination)){
               move_uploaded_file($_FILES['profile_picture']['tmp_name'], $destination);
            }

            $destination2 = $directory . "/photo-resize." . $ext;
            if(file_exists($destination2)){
               unlink($destination2);
            }
            $image = new ImageResize($destination);
            $image->resizeToWidth(750, 450);
            $image->save($destination2);

            $destination3 = $directory . "/photo-750x450." . $ext;
            if(file_exists($destination3)){
               unlink($destination3);
            }
            $image = new ImageResize($destination2);
            $image->crop(750, 450, 0, 450);
            $image->save($destination3);

            $mail = new  Message();

            $subject = "Welcome";
            $message = "Hi " . $first_name . ",\n\n";
            $message .= "You successfully registered, pls. login on " . $config['baseUrl'] . "/login \n";
            $message .= "If in case you need help, you can check out the URL below.\n\n";
            $message .= "\nOr you can reach us through " . $config['baseUrl'] . "/contact to serve you.\n";
            $message .= "\n\nThank You,\n";
            $message .= "Admin :)";

            $mail->setFrom($config['email']);
            $mail->addTo($email);
            $mail->setEncoding("UTF-8");
            $mail->setSubject($subject);
            $mail->setBody($message);

            $transport = new Sendmail();
            // $transport->send($mail);

            $this->flashMessenger()->setNamespace('success')->addMessage($role . " successfully registered.");
            return $this->redirect()->toRoute('login');
          }
        }else{
          // print_r($form->getMessages());
        }
      }else{
        if(!empty($countryCode)){
          $country = $this->getCountryMapper()->getCountryCode($countryCode);
          if($country){
            $form->get('country_id')->setValue($country->getId());
          }
        }

        $form->get('city')->setValue($city);
      }

      return new ViewModel(array(
        'form' => $form,
      ));
    }

    private function _getIP(){
      $config = $this->getServiceLocator()->get('Config');
      if (isset($_SERVER["HTTP_CF_CONNECTING_IP"])) {
        $_SERVER['REMOTE_ADDR'] = $_SERVER["HTTP_CF_CONNECTING_IP"];
      }
      if($_SERVER['REMOTE_ADDR'] == '127.0.0.1'){
        $_SERVER['REMOTE_ADDR'] = $config['ip'];
      }
    }

    public function viewAction()
    {
      $id = (int)$this->params('id');
      if (!$id) {
        return $this->redirect()->toRoute('home');
      }
      $user = $this->getUserMapper()->getUser($id);
      if(!$user){
        $this->flashMessenger()->setNamespace('error')->addMessage('Invalid User.');
        return $this->redirect()->toRoute('home');
      }
      $lastUserLocation = $this->getUserLocationMapper()->getUserLocationLast($user->getId());

      $organization = $this->getOrganizationMapper()->getOrganization($user->getOrganizationId());

      $form = $this->getServiceLocator()->get('VolunteerMessageForm');

      $currentUser = array();
      $authService = $this->serviceLocator->get('auth_service');
      if(isset($authService->getIdentity()->id)){
        $currentUser = $this->getUserMapper()->getUser($authService->getIdentity()->id);
        if(!$currentUser){
          $this->flashMessenger()->setNamespace('error')->addMessage('Invalid User.');
          return $this->redirect()->toRoute('home');
        }
      }

      $request = $this->getRequest();
      if ($request->isPost()) {
        $form->setData($request->getPost()->toArray());
        if ($form->isValid()) {
          $data = $form->getData();

          $config = $this->getServiceLocator()->get('Config');

          $subject = "O.F.W. Inquiry.";
          $message = $data['message'] . "\n\n" . $_SERVER['REMOTE_ADDR'];

          $mail = new  Message();

          // $mail->setFrom($currentUser->getEmail());
          $mail->setFrom($currentUser->getEmail());
          $mail->addTo($user->getEmail());
          $mail->setEncoding("UTF-8");
          $mail->setSubject($subject);
          $mail->setBody($message);

          $transport = new Sendmail();
          $transport->send($mail);

          $sender = 'HackTheHive';
          $numbers = array('639086087306');
          $serviceSms = $this->getServiceLocator()->get('ServiceSms');
          // $serviceSms->sendSms($sender, $numbers, $message);

          $this->flashMessenger()->setNamespace('success')->addMessage('Thank you. Message sent.');
          return $this->redirect()->toRoute('community', array('action' => 'view', 'id' => $id,));
        }else{
          print_r($form->getMessages());
        }
	    }

  		return new ViewModel(array(
        'form' => $form,
        'user' => $user,
        'lastUserLocation' => $lastUserLocation,
        'organization' => $organization,
        'currentUser' => $currentUser,
  		));
    }
}

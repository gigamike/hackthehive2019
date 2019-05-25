<?php

namespace Api\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\View\Model\JsonModel;
use Zend\Mail;
use Zend\Mail\Message;
use Zend\Mail\Transport\Sendmail;
use Zend\Authentication\Adapter\DbTable\CredentialTreatmentAdapter as AuthAdapter;

use GeoIp2\Database\Reader;
use Gumlet\ImageResize;

use User\Model\UserEntity;
use User\Model\UserLocationEntity;

class IndexController extends AbstractActionController
{
	private $_intervalInSeconds = 300;

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

	/*
	* https://apitester.com/
	*
	*/
	public function indexAction()
	{
		$config = $this->getServiceLocator()->get('Config');

		return new ViewModel(array(
			'config' => $config,
    ));
	}

	private function _getResponseWithHeader()
  {
    $response = $this->getResponse();
    $response->getHeaders()
       // make can accessed by *
       ->addHeaderLine('Access-Control-Allow-Origin','*')
       // set allow methods
       ->addHeaderLine('Access-Control-Allow-Methods','POST PUT DELETE GET')
			 // json
			 ->addHeaderLine('Content-Type', 'application/json');
    return $response;
  }

	/*
	* http://hackthehive2019.gigamike.net/api/geoip
	*
	*/
	public function geoipAction()
	{
		$results = array();

		$ip = $this->params()->fromQuery('ip');
		if(!empty($ip)){
			if (filter_var($ip, FILTER_VALIDATE_IP)) {
				$_SERVER['REMOTE_ADDR'] = $ip;
			}else{
				$results['error'] = 'Invalid IP';
			}
		}else{
			$this->_getIP();
		}

		if(count($results) <= 0){
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
		}

		$results = array(
			'country_code' => $record->country->isoCode,
			'country_name' => $record->country->name,
			'latitude' => $record->location->latitude,
			'longitude' => $record->location->longitude,
			'city' => $record->city->name,
		);

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
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

	/*
	* http://hackthehive2019.gigamike.net/api/organizations
	*
	*/
	public function organizationsAction()
	{
		$results = array();

		$organizations = $this->getOrganizationMapper()->fetchAll(false, array(), array('organization'));

		if(count($organizations) > 0){
			foreach ($organizations as $organization) {
				$results[] = array(
					'id' => $organization->getId(),
					'organization' => $organization->getOrganization(),
				);
			}
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/countries
	*
	*/
	public function countriesAction()
	{
		$results = array();

		$countries = $this->getCountryMapper()->fetchAll(false, array(), array('country_name'));

		if(count($countries) > 0){
			foreach ($countries as $country) {
				$results[] = array(
					'id' => $country->getId(),
					'country_code' => $country->getCountryCode(),
					'country_name' => $country->getCountryName(),
				);
			}
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/volunteers
	*
	*/
	public function volunteersAction()
	{
		$results = array();

		$config = $this->getServiceLocator()->get('Config');

		$user_id = $this->params()->fromQuery('user_id');
		$first_name_keyword = $this->params()->fromQuery('first_name_keyword');
		$last_name_keyword = $this->params()->fromQuery('last_name_keyword');
		$organization_id = $this->params()->fromQuery('organization_id');
		$country_code = $this->params()->fromQuery('country_code');
		$city_keyword = $this->params()->fromQuery('city_keyword');
		$page = $this->params()->fromQuery('page');
		$item_count_per_page = $this->params()->fromQuery('item_count_per_page');

		if(empty($page) && !is_numeric($page)){
			$page = 1;
		}
		if(empty($item_count_per_page) && !is_numeric($item_count_per_page)){
			$item_count_per_page = 10;
		}

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

		$filter = array();
		$filter['role'] = 'volunteer';
		$filter['latitude'] = $latitude;
		$filter['longitude'] = $longitude;
		if(!empty($user_id)){
			$filter['user_id'] = $user_id;
		}
		if(!empty($first_name_keyword)){
			$filter['first_name_keyword'] = $first_name_keyword;
		}
		if(!empty($last_name_keyword)){
			$filter['last_name_keyword'] = $last_name_keyword;
		}
		if(!empty($organization_id)){
			$filter['organization_id'] = $organization_id;
		}
		if(!empty($country_code)){
			$filter['country_code'] = $country_code;
		}
		if(!empty($city_keyword)){
			$filter['city_keyword'] = $city_keyword;
		}

		$order = array(
			'distance',
			'first_name',
			'last_name',
		);
		$volunteers = $this->getUserMapper()->getVolunteers(true, $filter, $order);
		$volunteers->setCurrentPageNumber($page);
		$volunteers->setItemCountPerPage($item_count_per_page);

		$results['total'] = $volunteers->getTotalItemCount();
		$results['page'] = $page;
		$results['item_count_per_page'] = $item_count_per_page;

		if(count($volunteers) > 0){
			foreach ($volunteers as $volunteer) {

				if($volunteer['profile_pic']){
					$ext = pathinfo($volunteer['profile_pic'], PATHINFO_EXTENSION);
					$profile_pic = $config['baseUrl'] . "img/user/" . $volunteer['id'] . "/photo-750x450." . $ext;
				}else{
					$profile_pic = "http://placehold.it/750x450";
				}

				$lastLoginTime = strtotime($volunteer['last_login_datetime']);
				$interval = time() - $lastLoginTime;
				if($interval <= $this->_intervalInSeconds){
					$isOnline = true;
				}else{
					$isOnline = false;
				}

				$results['results'][] = array(
					'id' => $volunteer['id'],
					'first_name' => $volunteer['first_name'],
					'middle_name' => $volunteer['middle_name'],
					'last_name' => $volunteer['last_name'],
					'organization' => $volunteer['organization'],
					'profile_pic' => $profile_pic,
					'country' => $volunteer['country_name'],
					'city' => $volunteer['city'],
					'distance' => round($volunteer['distance'], 2),
					'is_online' => $isOnline,
					'mobile_no' => $volunteer['mobile_no'],
					'latitude' => $volunteer['latitude'],
					'longitude' => $volunteer['longitude'],
					'ip' => $volunteer['ip'],
				);
			}
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/volunteers
	*
	*/
	public function ofwsAction()
	{
		$results = array();

		$config = $this->getServiceLocator()->get('Config');

		$user_id = $this->params()->fromQuery('user_id');
		$first_name_keyword = $this->params()->fromQuery('first_name_keyword');
		$last_name_keyword = $this->params()->fromQuery('last_name_keyword');
		$country_code = $this->params()->fromQuery('country_code');
		$city_keyword = $this->params()->fromQuery('city_keyword');
		$page = $this->params()->fromQuery('page');
		$item_count_per_page = $this->params()->fromQuery('item_count_per_page');

		if(empty($page) && !is_numeric($page)){
			$page = 1;
		}
		if(empty($item_count_per_page) && !is_numeric($item_count_per_page)){
			$item_count_per_page = 10;
		}

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

		$filter = array();
		$filter['role'] = 'ofw';
		$filter['latitude'] = $latitude;
		$filter['longitude'] = $longitude;
		if(!empty($user_id)){
			$filter['user_id'] = $user_id;
		}
		if(!empty($first_name_keyword)){
			$filter['first_name_keyword'] = $first_name_keyword;
		}
		if(!empty($last_name_keyword)){
			$filter['last_name_keyword'] = $last_name_keyword;
		}
		if(!empty($country_code)){
			$filter['country_code'] = $country_code;
		}
		if(!empty($city_keyword)){
			$filter['city_keyword'] = $city_keyword;
		}

		$order = array(
			'distance',
			'first_name',
			'last_name',
		);
		$ofws = $this->getUserMapper()->getOfws(true, $filter, $order);
		$ofws->setCurrentPageNumber($page);
		$ofws->setItemCountPerPage($item_count_per_page);

		$results['total'] = $ofws->getTotalItemCount();
		$results['page'] = $page;
		$results['item_count_per_page'] = $item_count_per_page;

		if(count($ofws) > 0){
			foreach ($ofws as $ofw) {

				if($ofw['profile_pic']){
					$ext = pathinfo($ofw['profile_pic'], PATHINFO_EXTENSION);
					$profile_pic = $config['baseUrl'] . "img/user/" . $ofw['id'] . "/photo-750x450." . $ext;
				}else{
					$profile_pic = "http://placehold.it/750x450";
				}

				$lastLoginTime = strtotime($ofw['last_login_datetime']);
				$interval = time() - $lastLoginTime;
				if($interval <= $this->_intervalInSeconds){
					$isOnline = true;
				}else{
					$isOnline = false;
				}

				$results['results'][] = array(
					'id' => $ofw['id'],
					'first_name' => $ofw['first_name'],
					'middle_name' => $ofw['middle_name'],
					'last_name' => $ofw['last_name'],
					'profile_pic' => $profile_pic,
					'country' => $ofw['country_name'],
					'city' => $ofw['city'],
					'distance' => round($ofw['distance'], 2),
					'is_online' => $isOnline,
					'mobile_no' => $volunteer['mobile_no'],
					'latitude' => $volunteer['latitude'],
					'longitude' => $volunteer['longitude'],
					'ip' => $volunteer['ip'],
				);
			}
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/ofw-registration
	*
	*/
	public function ofwRegistrationAction()
	{
		$results = array();
		$errors = array();

		$config = $this->getServiceLocator()->get('Config');

		$this->_getIP();

		if($this->getRequest()->isPost()) {
      $data = $this->params()->fromPost();

			$email = trim($data['email']);
			$password = trim($data['password']);
			$first_name = trim($data['first_name']);
			$middle_name = trim($data['middle_name']);
			$last_name = trim($data['last_name']);
			$country_id = trim($data['country_id']);
			$city = trim($data['city']);
			$mobile_no = trim($data['mobile_no']);

			if(empty($email)){
  			$results['error']['email'] = 'Required field email.';
			}elseif(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
				$results['error']['email'] = 'Invalid email.';
  		}else{
				$user = $this->getUserMapper()->getUserByEmail($email);
				if($user){
					$results['error']['email'] = 'Email already exists.';
				}
			}

			if(empty($password)){
  			$results['error']['password'] = 'Required field password.';
			}elseif(strlen($password) < 5){
				$results['error']['password'] = 'Invalid password. Password at least 5 characters';
			}

			if(empty($first_name)){
  			$results['error']['first_name'] = 'Required field first name.';
			}
			if(empty($middle_name)){
  			$results['error']['middle_name'] = 'Required field middle name.';
			}
			if(empty($last_name)){
  			$results['error']['last_name'] = 'Required field last name.';
			}

			if(empty($country_id)){
  			$results['error']['country'] = 'Required field country.';
			}else{
				$country = $this->getCountryMapper()->getCountry($country_id);
				if(!$country){
					$results['error']['country'] = 'Country does not exists.';
				}
			}
			if(empty($city)){
				$results['error']['city'] = 'Required field city.';
			}
			if(empty($mobile_no)){
  			$results['error']['mobile_no'] = 'Required field mobile.';
			}

			if(!isset($_FILES['profile_picture'])){
				$results['error']['profile_picture'] = 'Required field Profile Picture.';
			}else{
				$allowed =  array('png','jpeg','jpg');
				$ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
				if(!in_array($ext, $allowed) ) {
					$results['error']['profile_picture'] = "File type not allowed. Only " . implode(',', $allowed);
				}
				switch ($_FILES['profile_picture']['error']){
					case 1:
						$results['error']['profile_picture'] = 'The file is bigger than this PHP installation allows.';
						break;
					case 2:
						$results['error']['profile_picture'] = 'The file is bigger than this form allows.';
						break;
					case 3:
						$results['error']['profile_picture'] = 'Only part of the file was uploaded.';
						break;
					case 4:
						$results['error']['profile_picture'] = 'No file was uploaded.';
						break;
					default:
				}
			}

			if(!isset($results['error'])){
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

				$user = new UserEntity();
				$user->setRole('ofw');
				$user->setActive('Y');
				$user->setEmail($email);
				$user->setFirstName($first_name);
				$user->setMiddleName($middle_name);
				$user->setLastName($last_name);
				$user->setOrganizationId(0);
				$user->setCountryId($country_id);
				$user->setCity($city);
				$user->setMobileNo($mobile_no);

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

				$subject = "Welcome";
				$message = "Hi " . $first_name . ",\n\n";
				$message .= "You successfully registered, pls. login on " . $config['baseUrl'] . "/login \n";
				$message .= "If in case you need help, you can check out the URL below.\n\n";
				$message .= "\nOr you can reach us through " . $config['baseUrl'] . "/contact to serve you.\n";
				$message .= "\n\nThank You,\n";
				$message .= "Admin :)";

				try {
					$mail = new Message();
					$mail->setFrom($config['email']);
					$mail->addTo($user->getEmail());
					$mail->setSubject($subject);
					$mail->setBody($message);

					$mail->setFrom($config['email']);
					$mail->addTo($email);
					$mail->setEncoding("UTF-8");
					$mail->setSubject($subject);
					$mail->setBody($message);

					$transport = new Sendmail();
					// $transport->send($mail);
				} catch(\Exception $e) {
				}

				$results['success'] = 'OFW successfully added.';
			}
		}else{
			$results['error']['method'] = 'Invalid method.';
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/volunteer-registration
	*
	*/
	public function volunteerRegistrationAction()
	{
		$results = array();
		$errors = array();

		$config = $this->getServiceLocator()->get('Config');

		$this->_getIP();

		if($this->getRequest()->isPost()) {
      $data = $this->params()->fromPost();

			$email = trim($data['email']);
			$password = trim($data['password']);
			$organization_id = trim($data['organization_id']);
			$first_name = trim($data['first_name']);
			$middle_name = trim($data['middle_name']);
			$last_name = trim($data['last_name']);
			$country_id = trim($data['country_id']);
			$city = trim($data['city']);
			$mobile_no = trim($data['mobile_no']);

			if(empty($email)){
  			$results['error']['email'] = 'Required field email.';
			}elseif(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
				$results['error']['email'] = 'Invalid email.';
  		}else{
				$user = $this->getUserMapper()->getUserByEmail($email);
				if($user){
					$results['error']['email'] = 'Email already exists.';
				}
			}

			if(empty($password)){
  			$results['error']['password'] = 'Required field password.';
			}elseif(strlen($password) < 5){
				$results['error']['password'] = 'Invalid password. Password at least 5 characters';
			}

			if(empty($organization_id)){
  			$results['error']['organization'] = 'Required field organization.';
			}else{
				$organization = $this->getOrganizationMapper()->getOrganization($organization_id);
				if(!$organization){
					$results['error']['organization'] = 'Organization does not exists.';
				}
			}

			if(empty($first_name)){
  			$results['error']['first_name'] = 'Required field first name.';
			}
			if(empty($middle_name)){
  			$results['error']['middle_name'] = 'Required field middle name.';
			}
			if(empty($last_name)){
  			$results['error']['last_name'] = 'Required field last name.';
			}

			if(empty($country_id)){
  			$results['error']['country'] = 'Required field country.';
			}else{
				$country = $this->getCountryMapper()->getCountry($country_id);
				if(!$country){
					$results['error']['country'] = 'Country does not exists.';
				}
			}
			if(empty($city)){
				$results['error']['city'] = 'Required field city.';
			}
			if(empty($mobile_no)){
  			$results['error']['mobile_no'] = 'Required field mobile.';
			}

			if(!isset($_FILES['profile_picture'])){
				$results['error']['profile_picture'] = 'Required field Profile Picture.';
			}else{
				$allowed =  array('png','jpeg','jpg');
				$ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
				if(!in_array($ext, $allowed) ) {
					$results['error']['profile_picture'] = "File type not allowed. Only " . implode(',', $allowed);
				}
				switch ($_FILES['profile_picture']['error']){
					case 1:
						$results['error']['profile_picture'] = 'The file is bigger than this PHP installation allows.';
						break;
					case 2:
						$results['error']['profile_picture'] = 'The file is bigger than this form allows.';
						break;
					case 3:
						$results['error']['profile_picture'] = 'Only part of the file was uploaded.';
						break;
					case 4:
						$results['error']['profile_picture'] = 'No file was uploaded.';
						break;
					default:
				}
			}

			if(!isset($results['error'])){
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

				$user = new UserEntity();
				$user->setRole('volunteer');
				$user->setActive('Y');
				$user->setEmail($email);
				$user->setOrganizationId($organization_id);
				$user->setFirstName($first_name);
				$user->setMiddleName($middle_name);
				$user->setLastName($last_name);
				$user->setOrganizationId(0);
				$user->setCountryId($country_id);
				$user->setCity($city);
				$user->setMobileNo($mobile_no);

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

				$subject = "Welcome";
				$message = "Hi " . $first_name . ",\n\n";
				$message .= "You successfully registered, pls. login on " . $config['baseUrl'] . "/login \n";
				$message .= "If in case you need help, you can check out the URL below.\n\n";
				$message .= "\nOr you can reach us through " . $config['baseUrl'] . "/contact to serve you.\n";
				$message .= "\n\nThank You,\n";
				$message .= "Admin :)";

				try {
					$mail = new Message();
					$mail->setFrom($config['email']);
					$mail->addTo($user->getEmail());
					$mail->setSubject($subject);
					$mail->setBody($message);

					$mail->setFrom($config['email']);
					$mail->addTo($email);
					$mail->setEncoding("UTF-8");
					$mail->setSubject($subject);
					$mail->setBody($message);

					$transport = new Sendmail();
					// $transport->send($mail);
				} catch(\Exception $e) {
				}

				$results['user_id'] = $user->getId();
				$results['success'] = 'OFW successfully added.';
			}
		}else{
			$results['error']['method'] = 'Invalid method.';
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/login
	*
	*/
	public function loginAction()
	{
		$results = array(
			'is_logged' => false,
		);
		$errors = array();

		$config = $this->getServiceLocator()->get('Config');

		if($this->getRequest()->isPost()) {
      $data = $this->params()->fromPost();

			$email = trim($data['email']);
			$password = trim($data['password']);

			if(empty($email)){
  			$results['error']['email'] = 'Required field email.';
			}elseif(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
				$results['error']['email'] = 'Invalid email.';
  		}else{
				$user = $this->getUserMapper()->getUserByEmail($email);
				if(!$user){
					$results['error']['email'] = 'User does not exists.';
				}
			}

			if(empty($password)){
  			$results['error']['password'] = 'Required field password.';
			}

			if(!isset($results['error'])){
				$authService = $this->serviceLocator->get('auth_service');

				$dbAdapter = $this->serviceLocator->get('Zend\Db\Adapter\Adapter');
				$authAdapter = new AuthAdapter(
					$dbAdapter,
					'user',
					'email',
					'password',
					"MD5(CONCAT('" . $config['staticSalt'] . "', ?, salt)) AND active='Y'"
				);
				$authAdapter->setIdentity($email)->setCredential($password);
				$result = $authService->authenticate($authAdapter);
				if($result->isValid()) {
					$results['is_logged'] = true;
					$results['user_id'] = $authAdapter->getResultRowObject()->id;
					$results['role'] = $authAdapter->getResultRowObject()->role;
					$results['success'] = 'Successfully logged in.';
				}else{
					$results['error']['password'] = 'Invalid credentials.';
				}
			}
		}else{
			$results['error']['method'] = 'Invalid method.';
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/user-update
	*
	*/
	public function userUpdateAction()
	{
		$results = array();
		$errors = array();

		$config = $this->getServiceLocator()->get('Config');

		$this->_getIP();

		$user_id = $this->params()->fromQuery('user_id');
		if(!empty($user_id)){
			$user = $this->getUserMapper()->getUser($user_id);
			if($user){
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

				$results['success'] = 'User location and logged in successfully updated.';
			}else{
				$results['error']['user'] = 'Invalid user.';
			}
		}else{
			$results['error']['user'] = 'Invalid user.';
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	/*
	* http://hackthehive2019.gigamike.net/api/message
	*
	*/
	public function messageAction()
	{
		$results = array();
		$errors = array();

		$config = $this->getServiceLocator()->get('Config');

		if($this->getRequest()->isPost()) {
      $data = $this->params()->fromPost();

			$sender_user_id = trim($data['sender_user_id']);
			$recipient_user_id = trim($data['recipient_user_id']);
			$message = trim($data['message']);

			if(empty($sender_user_id)){
  			$results['error']['sender_user_id'] = 'Required field sender_user_id.';
			}else{
				$userSender = $this->getUserMapper()->getUser($sender_user_id);
				if(!$userSender){
					$results['error']['sender_user_id'] = 'Sender user does not exists.';
				}
			}

			if(empty($recipient_user_id)){
  			$results['error']['recipient_user_id'] = 'Required field recipient_user_id.';
			}else{
				$userRecipient = $this->getUserMapper()->getUser($recipient_user_id);
				if(!$userRecipient){
					$results['error']['recipient_user_id'] = 'Recipient user does not exists.';
				}
			}

			if(empty($message)){
  			$results['error']['message'] = 'Required field message.';
			}

			if(!isset($results['error'])){
				$subject = "HackTheHive2019 Inquiry.";
				$message = $message . "\n\n" . $_SERVER['REMOTE_ADDR'];

				$mail = new  Message();

				// $mail->setFrom($currentUser->getEmail());
				$mail->setFrom($userSender->getEmail());
				$mail->addTo($userRecipient->getEmail());
				$mail->setEncoding("UTF-8");
				$mail->setSubject($subject);
				$mail->setBody($message);

				$transport = new Sendmail();
				// $transport->send($mail);

				// $sender = 'HackTheHive';
				// $numbers = array('639086087306');
				$serviceSms = $this->getServiceLocator()->get('ServiceSms');
				// $serviceSms->sendSms($sender, $numbers, $message);

				$sender = 'HackTheHive';
				$number = '639086087306';
				$serviceSms->sendSms($sender, $number, $message);

				$results['success'] = 'Message send.';
			}
		}else{
			$results['error']['method'] = 'Invalid method.';
		}

		$response = $this->_getResponseWithHeader()->setContent(json_encode($results));
    return $response;
	}

	private function _curl($url, $post = null, $headers = array()){
    $ch = curl_init();

		$countPost = count($post);
		if(!is_null($post)){
			curl_setopt($ch, CURLOPT_POST, $countPost);
  		curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
		}

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_TIMEOUT, 30);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		if(count($headers) > 0){
			curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		}

    $curlHeader = curl_getinfo($ch);
    $curlResult = curl_exec($ch);
    $curlErrorMessage = curl_error($ch);
    $curlErrorNo = curl_errno($ch);
    curl_close($ch);

    $results = array();
    $results['headers'] = $curlHeader;
    $results['error_number'] = $curlErrorNo;
    $results['error_message'] =$curlErrorMessage;
    $results['result'] = $curlResult;

    return $results;
  }

	private function _checkApiAccess(){
		$config = $this->getServiceLocator()->get('Config');

		$isIPAllowed = false;
		$allowedIps = $config['media_api']['allowed_ips'];
		if(count($allowedIps) > 0){
	    foreach ($allowedIps as $allowedIp) {
	      if($_SERVER['REMOTE_ADDR'] == $allowedIp){
					$isIPAllowed = true;
	      }
	    }
	  }

		if($isIPAllowed){
			header("HTTP/1.0 404 Not Found");
		 	exit();
		}

		$headers = getallheaders();
		if(!isset($headers['Access-Key']) && !isset($headers['Secret-Key'])){
			return array(
				'access' => false,
				'error' => 'Required API access',
			);
		}else{
			if(($headers['Access-Key'] != $config['media_api']['access_key'])
				|| ($headers['Secret-Key'] != $config['media_api']['secret_key'])){
					return array(
						'access' => false,
						'error' => 'Invalid API access',
					);
			}
		}

		return array(
			'access' => true,
		);
	}
}

<?php

namespace Cron\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;

class IndexController extends AbstractActionController
{
  /*
  *
  * /Applications/MAMP/bin/php/php7.0.33/bin/php /Users/michaelgerardgalon/Sites/hackathons/hackthehive2019.gigamike.net/public_html/index.php cron-test
  * /opt/php71/bin/php /home3/gigamike/hackthehive2019.gigamike.net/public_html/index.php cron-test
  */
  public function indexAction()
  {
    // curl -X POST -d "email=volunteer2@gigamike.net&password=changeme"http://hackthehive2019.gigamike.net/api/login
    // curl -X POST -d "email=volunteer1@gigamike.net&password=changeme" http://hackthehive2019.gigamike.net/api/login
    /*
    $url = "http://hackthehive2019.gigamike.net/api/login";
    $post = array(
      'email' => 'volunteer2@gigamike.net',
      'password' => 'changeme',
    );
    $results = $this->_curl($url, $post);
    print_r($results);
    exit();
    */

    /*
    curl -X POST \
    -H "Content-Type: multipart/form-data" \
    -F "email=ofw3@gigamike.net" \
    -F "password=changeme" \
    -F "first_name=Mik" \
    -F "middle_name=Tupas" \
    -F "last_name=Galon" \
    -F "country_id=182" \
    -F "city=Pasay" \
    -F "mobile_no=649086087306" \
    -F "profile_picture=@/Users/michaelgerardgalon/Sites/hackathons/hackthehive2019.gigamike.net/public_html/img/user/1/photo-orig.jpg" \
    http://hackthehive2019.gigamike.net/api/ofw-registration
    */

    /*
    $basePath = getcwd();
    $profile_picture = $basePath . "/public_html/img/user/1/photo-orig.jpg";
    if (!function_exists('curl_file_create')) {
      function curl_file_create($filename, $mimetype = '', $postname = '') {
          return "@$filename;filename="
              . ($postname ?: basename($filename))
              . ($mimetype ? ";type=$mimetype" : '');
      }

      $profile_picture = curl_file_create($profile_picture, 'image/jpeg', 'profile_picture');
    }else{
      $profile_picture = new \CURLFile($profile_picture, 'image/jpeg', basename($profile_picture));
    }

    $url = "http://hackthehive2019.gigamike.net/api/ofw-registration";
    $post = array(
      'email' => 'ofw2@gigamike.net',
      'password' => 'changeme',
      'first_name' => 'Zeev',
      'middle_name' => 'Espedillon',
      'last_name' => 'Galon',
      'country_id' => '182',
      'city' => 'Pasay',
      'mobile_no' => '649086087306',
      'profile_picture' => $profile_picture,
    );
    // print_r($post);
    $headers = array('Content-Type: multipart/form-data');

    $results = $this->_curl($url, $post, $headers);
    print_r($results);
    */

    $basePath = getcwd();
    $profile_picture = $basePath . "/public_html/img/user/1/photo-orig.jpg";
    if (!function_exists('curl_file_create')) {
      function curl_file_create($filename, $mimetype = '', $postname = '') {
          return "@$filename;filename="
              . ($postname ?: basename($filename))
              . ($mimetype ? ";type=$mimetype" : '');
      }

      $profile_picture = curl_file_create($profile_picture, 'image/jpeg', 'profile_picture');
    }else{
      $profile_picture = new \CURLFile($profile_picture, 'image/jpeg', basename($profile_picture));
    }

    $url = "http://hackthehive2019.gigamike.net/api/volunteer-registration";
    $post = array(
      'email' => 'volunteer12@gigamike.net',
      'password' => 'changeme',
      'organization_id' => '1',
      'first_name' => 'Zeev',
      'middle_name' => 'Espedillon',
      'last_name' => 'Galon',
      'country_id' => '182',
      'city' => 'Pasay',
      'mobile_no' => '649086087306',
      'profile_picture' => $profile_picture,
    );
    // print_r($post);
    $headers = array('Content-Type: multipart/form-data');

    $results = $this->_curl($url, $post, $headers);
    print_r($results);
	}

  private function _curl($url, $post = null, $headers = array()){
    $ch = curl_init();

		$countPost = count($post);
		if(!is_null($post)){
			curl_setopt($ch, CURLOPT_POST, $countPost);
  		curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
		}

    curl_setopt($ch, CURLOPT_URL, $url);
    // curl_setopt($ch, CURLOPT_TIMEOUT, 30);
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
}

<?php

namespace FreeBeeApi\Service;

use Zend\Mvc\Controller\AbstractActionController;

class ServiceFreeBeeApi extends AbstractActionController
{
	private $_baseUrl = 'https://techdata.smart.com.ph';
	private $_pin = '25943419612892';
	private $_freeBeeNumber = '639086087306';

	public function getToken()
	{
		$url = $this->_baseUrl . "/token";
		$headers = array(
			'authorization: Basic YzZUZkk4Nm9hYWxFdzFmX1ZYclhuRGxZZklNYTpicFB5S19uSEFBUkphY2lQSmlJTUJIaTQ4ZWdh',
			'content-type: application/x-www-form-urlencoded',
			'accept: application/json',
			'Content-Type:application/json',
		);
		$post = array(
			"grant_type" => "client_credentials",
		);

		$results = $this->_curl($url, $post, $headers);
		if(isset($results['result'])){
			$data = json_decode($results['result'], true);
			if(isset($data['access_token'])){
				return $data['access_token'];
			}
		}

		return null;
  }

	public function checkRegistrationStatus($token)
	{
		$url = $this->_baseUrl . "/sandbox/t/pldtglobal.com/hackathon-api/1.0.0/freebee/check-account/" . $this->_freeBeeNumber;
		$headers = array(
			"authorization: Bearer " . $token,
			'accept: application/json',
		);

		$results = $this->_curl($url, null, $headers);
		if(isset($results['result'])){
			$data = json_decode($results['result'], true);
			return $data;
		}

		return null;
	}

	public function checkRegistrationStatusAndEnrolledProducts($token){
		$url = $this->_baseUrl . "/sandbox/t/pldtglobal.com/hackathon-api/1.0.0/freebee/check-account-enrollment/" . $this->_freeBeeNumber;
		$headers = array(
			"authorization: Bearer " . $token,
			'accept: application/json',
		);

		$results = $this->_curl($url, null, $headers);
		if(isset($results['result'])){
			$data = json_decode($results['result'], true);
			return $data;
		}

		return null;
	}

	public function registerAndEnrollVoucherPIN($token){
		$url = $this->_baseUrl . "/sandbox/t/pldtglobal.com/hackathon-api/1.0.0/freebee/enroll/" . $this->_pin;
		$headers = array(
			"authorization: Bearer " . $token,
			'accept: application/json',
		);

		$results = $this->_curl($url, null, $headers);
		if(isset($results['result'])){
			$data = json_decode($results['result'], true);
			return $data;
		}

		return null;
	}

	private function _curl($url, $post = null, $headers = array()){
		$parsedUrl = parse_url($url);
    $scheme = isset($parsedUrl['scheme']) ? $parsedUrl['scheme'] : null;

    $ch = curl_init();

		$countPost = count($post);
		if(!is_null($post)){
			$postvars = '';
		  foreach($post as $key=>$value) {
		    $postvars .= $key . "=" . $value . "&";
		  }

			curl_setopt($ch, CURLOPT_POST, $countPost);
  		curl_setopt($ch, CURLOPT_POSTFIELDS, $postvars);
		}

		if($scheme == 'https'){
      curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
      curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 2);
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

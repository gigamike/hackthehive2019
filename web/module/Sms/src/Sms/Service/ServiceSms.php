<?php

namespace Sms\Service;

use Zend\Mvc\Controller\AbstractActionController;

class ServiceSms extends AbstractActionController
{
	private $_apiKey = 'IQOwZRNZ8WI-zqiNwiOVIxzVWif5111hJxOymxz9KH';

	/*
	* https://www.textlocal.com/integrations/api/#choose-language
	*/
	/*
	public function sendSms($sender, $numbers = array(), $message)
	{
		// Account details
		$apiKey = urlencode($this->_apiKey);

		// Message details
		$sender = urlencode($sender);
		$message = rawurlencode($message);

		$numbers = implode(',', $numbers);

		// Prepare data for POST request
		$data = array('apikey' => $apiKey, 'numbers' => $numbers, "sender" => $sender, "message" => $message);

		// Send the POST request with cURL
		$ch = curl_init('https://api.txtlocal.com/send/');
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($ch);
		curl_close($ch);

		// Process your response here
		// echo $response;
  }
	*/

	/*
	* https://semaphore.co/docs
	*/
	/*
	public function sendSms($sender, $number, $message)
	{
		$ch = curl_init();
		$parameters = array(
    'apikey' => '9bd26153a1b26d735782e8cd977b48d3',
    'number' => $number,
    'message' => $message,
    'sendername' => 'HackTheHive2019'
		);
		curl_setopt( $ch, CURLOPT_URL,'https://semaphore.co/api/v4/messages' );
		curl_setopt( $ch, CURLOPT_POST, 1 );

		//Send the parameters set above with the request
		curl_setopt( $ch, CURLOPT_POSTFIELDS, http_build_query( $parameters ) );

		// Receive response from server
		curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
		$output = curl_exec( $ch );
		curl_close ($ch);

		//Show the server response
		echo $output;
	}
	*/
}

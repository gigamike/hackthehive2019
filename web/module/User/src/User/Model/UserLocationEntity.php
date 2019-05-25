<?php
namespace User\Model;

class UserLocationEntity
{
	protected $id;
	protected $user_id;
	protected $ip;
	protected $country_code;
	protected $country_name;
	protected $city;
	protected $latitude;
	protected $longitude;
	protected $created_datetime;

	public function __construct()
	{
		$this->created_datetime = date('Y-m-d H:i:s');
	}

	public function getId()
	{
		return $this->id;
	}

	public function setId($value)
	{
		$this->id = $value;
	}

	public function getUserId()
	{
		return $this->user_id;
	}

	public function setUserId($value)
	{
		$this->user_id = $value;
	}

	public function getIp()
	{
	    return $this->ip;
	}

	public function setIp($value)
	{
	    $this->ip = $value;
	}

	public function getCountryCode()
	{
	    return $this->country_code;
	}

	public function setCountryCode($value)
	{
	    $this->country_code = $value;
	}

	public function getCountryName()
	{
	    return $this->country_name;
	}

	public function setCountryName($value)
	{
	    $this->country_name = $value;
	}

	public function getCity()
	{
	    return $this->city;
	}

	public function setCity($value)
	{
	    $this->city = $value;
	}

	public function getLatitude()
	{
	    return $this->latitude;
	}

	public function setLatitude($value)
	{
	    $this->latitude = $value;
	}

	public function getLongitude()
	{
		return $this->longitude;
	}

	public function setLongitude($value)
	{
		$this->longitude = $value;
	}

	public function getCreatedDatetime()
	{
		return $this->created_datetime;
	}

	public function setCreatedDatetime($value)
	{
		$this->created_datetime = $value;
	}
}

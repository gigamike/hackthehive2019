<?php
namespace Language\Model;

class LanguageEntity
{
	protected $id;
	protected $language;
	protected $code;

	public function getId()
	{
		return $this->id;
	}

	public function setId($value)
	{
		$this->id = $value;
	}

	public function getLanguage()
	{
		return $this->language;
	}

	public function setLanguage($value)
	{
		$this->language = $value;
	}
	
	public function getCode()
	{
		return $this->code;
	}
	
	public function setCode($value)
	{
		$this->code = $value;
	}
}
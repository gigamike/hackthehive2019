<?php
namespace Volunteer\Model;

class OrganizationEntity
{
	protected $id;
	protected $organization;

	public function getId()
	{
		return $this->id;
	}

	public function setId($value)
	{
		$this->id = $value;
	}

	public function getOrganization()
	{
		return $this->organization;
	}

	public function setOrganization($value)
	{
		$this->organization = $value;
	}
}

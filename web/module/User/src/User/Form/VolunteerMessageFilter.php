<?php

namespace User\Form;

use Zend\InputFilter\InputFilter;

class VolunteerMessageFilter extends InputFilter
{
	public function __construct()
	{
		$this->add(array(
			'name' => 'message',
			'required' => true,
		));
	}
}

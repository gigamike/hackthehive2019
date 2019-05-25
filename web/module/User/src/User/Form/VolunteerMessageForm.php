<?php

namespace User\Form;

use Zend\Form\Form;

class VolunteerMessageForm extends Form
{
	public function __construct($dbAdapter)
	{
		parent::__construct('volunteer-message');
		$this->setAttribute('method', 'post');
		$this->setInputFilter(new VolunteerMessageFilter($dbAdapter));

		$this->add(array(
			'name' => 'message',
			'type' => 'Textarea',
			'attributes' => array(
				'class' => 'form-control',
				'id' => 'message',
				'rows' => '10',
				'cols' => '100',
				'required' => 'required',
				'data-validation-required-message' => 'Please enter your message',
				'maxlength' => '999',
				'style' => 'resize:none',
			),
			'options' => array(
				'label' => 'Message:',
			),
		));

		$this->add(array(
			'name' => 'submit',
			'type' => 'Submit',
			'attributes' => array(
				'class' => 'btn btn-primary',
				'value' => 'Submit',
			),
		));
	}
}

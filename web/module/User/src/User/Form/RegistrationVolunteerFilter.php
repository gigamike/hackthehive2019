<?php

namespace User\Form;

use Zend\InputFilter\InputFilter;
use Zend\Validator\Hostname as HostnameValidator;

class RegistrationVolunteerFilter extends InputFilter
{
	public function __construct($dbAdapter)
	{
		$email = array(
			'name' => 'email',
			'required' => true,
			'validators' => array(
				array(
					'name' => 'EmailAddress',
					'options' => array(
						'allow' => HostnameValidator::ALLOW_DNS,
						'domain' => true,
					),
				),
			),
		);

		if(isset($_POST['id'])){
	    $email['validators'][1] = array(
        'name' => '\Zend\Validator\Db\NoRecordExists',
        'options' => array(
          'table' => 'user',
          'field' => 'email',
          'adapter' => $dbAdapter,
          'exclude' => array(
            'field' => 'id',
            'value' => $_POST['id'],
          ),
        ),
	    );
		}else{
	    $email['validators'][1] = array(
        'name' => '\Zend\Validator\Db\NoRecordExists',
        'options' => array(
          'table' => 'user',
          'field' => 'email',
          'adapter' => $dbAdapter,
        ),
	    );
		}

		$this->add($email);

		$this->add(array(
			'name' => 'password',
			'required' => true,
			'validators' => array(
				array(
					'name' => 'StringLength',
					'options' => array(
						'encoding' => 'UTF-8',
						'min' => 3,
						'max' => 255,
					),
				),
			),
		));

		$this->add(array(
			'name' => 'password_confirm',
			'required' => true,
			'validators' => array(
				array(
					'name' => 'StringLength',
					'options' => array(
						'encoding' => 'UTF-8',
						'min' => 3,
						'max' => 255,
					),
				),
				array(
					'name' => 'Identical',
					'options' => array(
							'token' => 'password',
					),
				),
			),
		));

    $this->add(array(
      'name' => 'first_name',
      'required' => true,
      'filters' => array(
        array(
          'name' => 'StripTags',
        ),
      ),
      'validators' => array(
        array(
          'name' => 'StringLength',
          'options' => array(
            'encoding' => 'UTF-8',
            'min' => 1,
            'max' => 255,
          ),
        ),
      ),
    ));

		$this->add(array(
			'name' => 'middle_name',
			'required' => true,
			'filters' => array(
				array(
					'name' => 'StripTags',
				),
			),
			'validators' => array(
				array(
					'name' => 'StringLength',
					'options' => array(
						'encoding' => 'UTF-8',
						'min' => 1,
						'max' => 255,
					),
				),
			),
		));

		$this->add(array(
			'name' => 'last_name',
			'required' => true,
			'filters' => array(
				array(
					'name' => 'StripTags',
				),
			),
			'validators' => array(
				array(
					'name' => 'StringLength',
					'options' => array(
						'encoding' => 'UTF-8',
						'min' => 1,
						'max' => 255,
					),
				),
			),
		));

    $this->add(array(
      'name' => 'country_id',
      'required' => true,
    ));

		$this->add(array(
      'name' => 'country_id',
      'required' => true,
    ));

    $this->add(array(
      'name' => 'volunteer_id',
      'required' => true,
    ));

		$this->add(array(
			'name' => 'mobile_no',
			'required' => true,
		));

		$this->add(array(
	    'name' => 'profile_picture',
	    'required' => false,
		));
	}
}

<?php

namespace User\Form;

use Zend\Form\Form;
use Zend\Stdlib\Hydrator\ClassMethods;
use User\Form\RegistrationVolunteerFilter;

class RegistrationVolunteerForm extends Form
{
	public function __construct($dbAdapter, $countryMapper, $organizationMapper)
	{
		parent::__construct('registration');
		$this->setAttribute('method', 'post');
		$this->setInputFilter(new RegistrationVolunteerFilter($dbAdapter));
		$this->setAttribute('enctype', 'multipart/form-data');
		$this->setHydrator(new ClassMethods());

		$this->add(array(
			'name' => 'email',
			'type' => 'Email',
			'attributes' => array(
				'class' => 'form-control',
				'placeholder'  => 'Email',
				'id' => 'email',
			),
			'options' => array(
				'label' => 'Email *',
			),
		));

		$this->add(array(
			'name' => 'password',
			'type' => 'Password',
			'attributes' => array(
				'class' => 'form-control',
				'placeholder'  => 'Password',
				'id' => 'password',
			),
			'options' => array(
				'label' => 'Password *',
			),
		));

		$this->add(array(
			'name' => 'password_confirm',
			'type' => 'Password',
			'attributes' => array(
				'class' => 'form-control',
				'placeholder'  => 'Confirm Password',
				'id' => 'password_confirm',
			),
			'options' => array(
				'label' => 'Confirm Password *',
			),
		));

		$this->add(array(
	    'name' => 'organization_id',
	    'type' => 'Select',
	    'attributes' => array(
        'class' => 'form-control',
        'id' => 'organization_id',
        'options' => $this->_getOrganizations($organizationMapper),
	    ),
	    'options' => array(
        'label' => 'Organization *',
	    ),
		));

		$this->add(array(
	    'name' => 'first_name',
	    'type' => 'Text',
	    'attributes' => array(
        'class' => 'form-control',
        'placeholder'  => 'First Name',
        'id' => 'first_name',
        'required' => 'required',
	    ),
	    'options' => array(
        'label' => 'First Name *',
	    ),
		));

		$this->add(array(
	    'name' => 'middle_name',
	    'type' => 'Text',
	    'attributes' => array(
        'class' => 'form-control',
        'placeholder'  => 'Middle Name',
        'id' => 'middle_name',
        'required' => 'required',
	    ),
	    'options' => array(
        'label' => 'Middle Name *',
	    ),
		));

		$this->add(array(
	    'name' => 'last_name',
	    'type' => 'Text',
	    'attributes' => array(
        'class' => 'form-control',
        'placeholder'  => 'Last Name',
        'id' => 'first_name',
        'required' => 'required',
	    ),
	    'options' => array(
        'label' => 'Last Name *',
	    ),
		));

		$this->add(array(
	    'name' => 'country_id',
	    'type' => 'Select',
	    'attributes' => array(
        'class' => 'form-control',
        'id' => 'country_id',
        'options' => $this->_getCountries($countryMapper),
	    ),
	    'options' => array(
        'label' => 'Current Country *',
	    ),
		));

		$this->add(array(
	    'name' => 'city',
	    'type' => 'Text',
	    'attributes' => array(
        'class' => 'form-control',
        'placeholder'  => 'Current City',
        'id' => 'city',
        'required' => 'required',
	    ),
	    'options' => array(
        'label' => 'Current City *',
	    ),
		));

		$this->add(array(
	    'name' => 'mobile_no',
	    'type' => 'Text',
	    'attributes' => array(
        'class' => 'form-control',
        'placeholder'  => 'Mobile Number',
        'id' => 'city',
        'required' => 'required',
	    ),
	    'options' => array(
        'label' => 'Mobile Number *',
	    ),
		));

		$this->add(array(
	    'name' => 'profile_picture',
	    'attributes' => array(
				'class' => 'form-control',
        'type'  => 'file',
        'id' => 'profile_picture',
	    ),
	    'options' => array(
        'label' => 'Profile Picture *',
	    ),
		));

		$this->add(array(
			'name' => 'security',
			'type' => 'Csrf',
		));

		$this->add(array(
			'name' => 'captcha',
			'type' => 'Captcha',
			'attributes' => array(
				'class' => 'form-control',
				'id' => 'captcha',
				'placeholder'  => 'Please verify you are human.',
			),
			'options' => array(
				'label' => 'Security Code *',
				'captcha' => array(
					'class' => 'Dumb',
	        'wordLen' => 3,
				),
			),
		));

		$this->add(array(
			'name' => 'submit',
			'type' => 'Submit',
			'attributes' => array(
				'class' => 'btn btn-primary',
				'value' => 'I Agree on Terms and Conditions and Signup',
			),
		));
	}

	private function _getOrganizations($organizationMapper){
    $organizations = array(
      '' => 'Select Organization',
    );
    $filter = array();
    $order = array(
        'organization',
    );
    $temp = $organizationMapper->fetchAll(false, $filter, $order);
    foreach ($temp as $organization){
      $organizations[$organization->getId()] = $organization->getOrganization();
    }

    return $organizations;
	}

	private function _getCountries($countryMapper){
    $languages = array(
        '' => 'Select Country',
    );
    $filter = array();
    $order = array(
        'country_name',
    );
    $temp = $countryMapper->fetchAll(false, $filter, $order);
    foreach ($temp as $country){
        $languages[$country->getId()] = $country->getCountryName();
    }

    return $languages;
	}
}

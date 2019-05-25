<?php
namespace User;

use Zend\Authentication\AuthenticationService;

use User\Model\UserMapper;
use User\Model\UserLocationMapper;
use User\Form\RegistrationOfwForm;
use User\Form\RegistrationVolunteerForm;
use User\Form\ForgotPasswordForm;
use User\Form\LoginForm;
use User\Service\ServiceLastLogin;

use Volunteer\Model\OrganizationMapper;
use Country\Model\CountryMapper;

use User\Form\VolunteerMessageForm;

class Module
{
  public function getConfig()
  {
    return include __DIR__ . '/config/module.config.php';
  }

  public function getAutoloaderConfig()
  {
    return array(
      'Zend\Loader\StandardAutoloader' => array(
        'namespaces' => array(
          __NAMESPACE__ => __DIR__ . '/src/' . __NAMESPACE__,
        ),
      ),
    );
  }

  public function getServiceConfig()
  {
    return array(
      'factories' => array(
        'auth_service' =>  function($sm) {
          $authService = new AuthenticationService();
          return $authService;
        },
        'UserMapper' => function ($sm) {
          $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
          $mapper = new UserMapper($dbAdapter);
          return $mapper;
        },
        'UserLocationMapper' => function ($sm) {
          $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
          $mapper = new UserLocationMapper($dbAdapter);
          return $mapper;
        },
        'RegistrationOfwForm' => function ($sm) {
          $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');

          $countryMapper = new CountryMapper($dbAdapter);

          $form = new RegistrationOfwForm($dbAdapter, $countryMapper);
          return $form;
        },
        'RegistrationVolunteerForm' => function ($sm) {
          $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');

          $countryMapper = new CountryMapper($dbAdapter);
          $organizationMapper = new OrganizationMapper($dbAdapter);

          $form = new RegistrationVolunteerForm($dbAdapter, $countryMapper, $organizationMapper);
          return $form;
        },
        'ServiceLastLogin' => function ($sm) {
          $service = new ServiceLastLogin();

          return $service;
        },
        'VolunteerMessageForm' => function ($sm) {
          $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');

          $form = new VolunteerMessageForm($dbAdapter);
          return $form;
        },
      ),
    );
  }

  public function getViewHelperConfig() {
    return array(
      'factories' => array(
        'online' => function($sm){
          return new \User\View\Helper\Online($sm->getServiceLocator());
        },
      )
    );
  }
}

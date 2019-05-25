<?php

namespace Volunteer\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\Mail\Message as Message;
use Zend\Mail\Transport\Sendmail as Sendmail;

class IndexController extends AbstractActionController
{
    public function indexAction()
    {

      return new ViewModel(array(

      ));
    }
}

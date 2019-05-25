<?php

namespace User\View\Helper;

use Zend\View\Helper\AbstractHelper;

class Online extends AbstractHelper
{
  private $_sm;
  private $_intervalInSeconds = 300;

  public function __construct(\Zend\ServiceManager\ServiceManager $sm) {
      $this->_sm = $sm;
  }

  public function getSm() {
      return $this->_sm;
  }

  public function getUserMapper()
  {
      return $this->getSm()->get('UserMapper');
  }

	public function __invoke($userId)
	{
	    $html = "";

	    if($userId){
	        $user = $this->getUserMapper()->getUser($userId);
	        if($user){
	            $lastLoginTime = strtotime($user->getLastLoginDatetime());
	            $interval = time() - $lastLoginTime;
	            if($interval <= $this->_intervalInSeconds){
	                $html = "<span class=\"online\">Online</span>";
	            }else{
	                $html = "<span class=\"offline\">Offline</span>";
	            }
	        }
	    }

	    return $html;
	}
}

<?php

namespace Cron\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\Console\Request as ConsoleRequest;
use Zend\Console\Response as ConsoleResponse;

class FreeBeeApiController extends AbstractActionController
{
  /*
  *
  * /Applications/MAMP/bin/php/php7.0.33/bin/php /Users/michaelgerardgalon/Sites/hackathons/hackthehive2019.gigamike.net/public_html/index.php cron-free-bee-api-test
  * /opt/php71/bin/php /home3/gigamike/hackthehive2019.gigamike.net/public_html/index.php cron-free-bee-api-test
  */
  public function indexAction()
  {
    $request = $this->getRequest();

		if (!$request instanceof ConsoleRequest){
			throw new \RuntimeException('You can only use this action from a console!');
		}
		$basePath = getcwd();

		// avoid duplicate instance
		$logfile = $basePath . "/data/temp/cron-free-bee-api-test.txt";
		$fp = fopen($logfile, "w");
		if (flock($fp, LOCK_EX | LOCK_NB)) {
			 set_time_limit(0);

			 $serviceFreeBeeApi = $this->getServiceLocator()->get('ServiceFreeBeeApi');
			 $token = $serviceFreeBeeApi->getToken();
       // $results = $serviceFreeBeeApi->checkRegistrationStatus($token);
       // $results = $serviceFreeBeeApi->checkRegistrationStatusAndEnrolledProducts($token);
       $results = $serviceFreeBeeApi->checkRegistrationStatusAndEnrolledProducts($token);
       print_r($results);

			 flock($fp, LOCK_UN);
		} else {
				// echo "script still running...\n";
		}
	}
}

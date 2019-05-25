<?php
namespace User\Model;

use Zend\Db\Adapter\Adapter;
use Zend\Stdlib\Hydrator\ClassMethods;
use Zend\Db\Sql\Sql;
use Zend\Db\Sql\Select;
use Zend\Db\ResultSet\HydratingResultSet;
use Zend\Paginator\Adapter\DbSelect;
use Zend\Paginator\Paginator;
use Zend\Db\Sql\Expression;
use Zend\Db\Adapter\Driver\ResultInterface;
use Zend\Db\ResultSet\ResultSet;
use User\Model\UserLocationEntity;

class UserLocationMapper
{
	protected $tableName = 'user_location';
	protected $dbAdapter;
	protected $sql;

	public function __construct(Adapter $dbAdapter)
	{
		$this->dbAdapter = $dbAdapter;
		$this->sql = new Sql($dbAdapter);
		$this->sql->setTable($this->tableName);
	}

	public function fetchAll($paginated=false, $filter = array(), $order=array())
	{
		$select = $this->sql->select();
		$where = new \Zend\Db\Sql\Where();

		if(isset($filter['user_id'])){
			$where->equalTo("user_id", $filter['user_id']);
		}

		if (!empty($where)) {
			$select->where($where);
		}

		if(count($order) > 0 ){
		    $select->order($order);
		}

		// echo $select->getSqlString($this->dbAdapter->getPlatform());exit();

		$statement = $this->sql->prepareStatementForSqlObject($select);
		$results = $statement->execute();

		$entityPrototype = new UserLocationEntity();
		$hydrator = new ClassMethods();
		$resultset = new HydratingResultSet($hydrator, $entityPrototype);
		$resultset->initialize($results);

		if($paginated) {
			$paginatorAdapter = new DbSelect(
					$select,
					$this->dbAdapter,
					$resultset
			);
			$paginator = new Paginator($paginatorAdapter);
			return $paginator;
		}

		return $resultset;
	}

	public function saveUserLocation(UserLocationEntity $userLocation)
	{
		$hydrator = new ClassMethods();
		$data = $hydrator->extract($userLocation);

		if ($userLocation->getId()) {
			// update action
			$action = $this->sql->update();
			$action->set($data);
			$action->where(array('id' => $userLocation->getId()));
		} else {
			// insert action
			$action = $this->sql->insert();
			unset($data['id']);
			$action->values($data);
		}
		$statement = $this->sql->prepareStatementForSqlObject($action);
		$result = $statement->execute();

		if (!$userLocation->getId()) {
			$userLocation->setId($result->getGeneratedValue());
		}
		return $result;
	}

	public function getUserLocation($id)
	{
		$select = $this->sql->select();
		$select->where(array('id' => $id));

		$statement = $this->sql->prepareStatementForSqlObject($select);
		$result = $statement->execute()->current();
		if (!$result) {
			return null;
		}

		$hydrator = new ClassMethods();
		$userLocation = new UserLocationEntity();
		$hydrator->hydrate($result, $userLocation);

		return $userLocation;
	}

	public function getUserLocationByUserId($user_id)
	{
		$select = $this->sql->select();
		$select->where(array('user_id' => $user_id));

		$statement = $this->sql->prepareStatementForSqlObject($select);
		$result = $statement->execute()->current();
		if (!$result) {
			return null;
		}

		$hydrator = new ClassMethods();
		$userLocation = new UserLocationEntity();
		$hydrator->hydrate($result, $userLocation);

		return $userLocation;
	}

	public function getUserLocationLast($user_id)
	{
	    $select = $this->sql->select();
	    $select->where(array('user_id' => $user_id));
	    $select->order(array('created_datetime DESC'));
	    $select->limit(1);

	    $statement = $this->sql->prepareStatementForSqlObject($select);
	    $result = $statement->execute()->current();
	    if (!$result) {
	        return null;
	    }

	    $hydrator = new ClassMethods();
	    $userLocation = new UserLocationEntity();
	    $hydrator->hydrate($result, $userLocation);

	    return $userLocation;
	}

	public function deleteUserLocation($id)
	{
	    $delete = $this->sql->delete();
	    $delete->where(array('id' => $id));

	    $statement = $this->sql->prepareStatementForSqlObject($delete);
	    return $statement->execute();
	}
}

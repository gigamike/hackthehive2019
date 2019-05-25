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
use User\Model\UserEntity;

class UserMapper
{
	protected $tableName = 'user';
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

		if(isset($filter['id'])){
			$where->equalTo("id", $filter['id']);
		}

		if(isset($filter['email'])){
			$where->addPredicate(
					new \Zend\Db\Sql\Predicate\Like("email", "%" . $filter['email'] . "%")
			);
		}

		if (!empty($where)) {
			$select->where($where);
		}

		if(count($order) > 0){
		    $select->order($order);
		}

		// echo $select->getSqlString($this->dbAdapter->getPlatform());exit();

		if($paginated) {
		    $entityPrototype = new UserEntity();
		    $hydrator = new ClassMethods();
		    $resultset = new HydratingResultSet($hydrator, $entityPrototype);

			$paginatorAdapter = new DbSelect(
					$select,
					$this->dbAdapter,
					$resultset
			);
			$paginator = new Paginator($paginatorAdapter);
			return $paginator;
		}else{
		    $statement = $this->sql->prepareStatementForSqlObject($select);
		    $results = $statement->execute();

		    $entityPrototype = new UserEntity();
		    $hydrator = new ClassMethods();
		    $resultset = new HydratingResultSet($hydrator, $entityPrototype);
		    $resultset->initialize($results);
		}

		return $resultset;
	}

	public function saveUser(UserEntity $user)
	{
		$hydrator = new ClassMethods();
		$data = $hydrator->extract($user);

		if ($user->getId()) {
			// update action
			$action = $this->sql->update();
			$action->set($data);
			$action->where(array('id' => $user->getId()));
		} else {
			// insert action
			$action = $this->sql->insert();
			unset($data['id']);
			$action->values($data);
		}
		$statement = $this->sql->prepareStatementForSqlObject($action);
		$result = $statement->execute();

		if (!$user->getId()) {
			$user->setId($result->getGeneratedValue());
		}
		return $result;
	}

	public function getUser($id)
	{
		$select = $this->sql->select();
		$select->where(array('id' => $id));

		$statement = $this->sql->prepareStatementForSqlObject($select);
		$result = $statement->execute()->current();
		if (!$result) {
			return null;
		}

		$hydrator = new ClassMethods();
		$user = new UserEntity();
		$hydrator->hydrate($result, $user);

		return $user;
	}

	public function getUserByEmail($email)
	{
		$select = $this->sql->select();
		$select->where(array('email' => $email));

		$statement = $this->sql->prepareStatementForSqlObject($select);
		$result = $statement->execute()->current();
		if (!$result) {
			return null;
		}

		$hydrator = new ClassMethods();
		$user = new UserEntity();
		$hydrator->hydrate($result, $user);

		return $user;
	}

	public function dynamicSalt() {
		$dynamicSalt = '';
		for ($i = 0; $i < 50; $i++) {
			$dynamicSalt .= chr(rand(33, 126));
		}

		return $dynamicSalt;
	}

	public function randomPassword() {
		$alphabet = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789";
		$pass = array();
		$alphaLength = strlen($alphabet) - 1;
		for ($i = 0; $i < 8; $i++) {
			$n = rand(0, $alphaLength);
			$pass[] = $alphabet[$n];
		}
		return implode($pass); //turn the array into a string
	}

	public function deleteUser($id)
	{
	    $delete = $this->sql->delete();
	    $delete->where(array('id' => $id));

	    $statement = $this->sql->prepareStatementForSqlObject($delete);
	    return $statement->execute();
	}

	public function getVolunteers($paginated=false, $filter = array(), $order = array())
	{
    $select = $this->sql->select();
    $select->columns(array(
      'id',
      'first_name',
			'middle_name',
      'last_name',
      'profile_pic',
      'last_login_datetime',
			'mobile_no',
    ));
    $select->join(
      'organization',
      $this->tableName . ".organization_id = organization.id",
      array(
        'organization',
      ),
      $select::JOIN_INNER
    );
		$select->join(
      'user_location',
      $this->tableName . ".id = user_location.user_id",
      array(
        'latitude',
				'longitude',
				'country_code',
				'country_name',
				'city',
				'ip',
				/*
				'distance' => new \Zend\Db\Sql\Expression("SQRT(POW(69.1 * (user_location.latitude - " . $filter['latitude'] . "), 2)
						+ POW(69.1 * (" . $filter['longitude'] . " - user_location.longitude) * COS(user_location.latitude / 57.3), 2))"),
				*/
				// Note: The provided distance is in Miles. If you need Kilometers, use 6371 instead of 3959.
				'distance' => new \Zend\Db\Sql\Expression("( 6371 * acos( cos( radians(" . $filter['latitude'] . ") ) * cos( radians( user_location.latitude ) )
				   * cos( radians(user_location.longitude) - radians(" . $filter['longitude'] . ")) + sin(radians(" . $filter['latitude'] . "))
				   * sin( radians(user_location.latitude))))"),
      ),
      $select::JOIN_INNER
    );

    $where = new \Zend\Db\Sql\Where();

    if(isset($filter['id'])){
			$where->equalTo($this->tableName . ".id", $filter['id']);
		}

		if(isset($filter['id_not'])){
	    $where->notEqualTo($this->tableName . ".id", $filter['id_not']);
		}

		if(isset($filter['user_id']) && !empty($filter['user_id'])){
			$where->equalTo($this->tableName . ".id", $filter['user_id']);
		}

		if(isset($filter['role']) && !empty($filter['role'])){
	    $where->equalTo("role", $filter['role']);
		}

		if(isset($filter['organization_id']) && !empty($filter['organization_id'])){
	    $where->equalTo($this->tableName . ".organization_id", $filter['organization_id']);
		}

		if(isset($filter['country_code']) && !empty($filter['country_code'])){
	    $where->equalTo("user_location.country_code", $filter['country_code']);
		}

		if(isset($filter['city_keyword']) && !empty($filter['city_keyword'])){
	    $where->addPredicate(
        new \Zend\Db\Sql\Predicate\Like($this->tableName . ".city", "%" . $filter['city_keyword'] . "%")
	    );
		}

		if(isset($filter['first_name_keyword']) && !empty($filter['first_name_keyword'])){
	    $where->addPredicate(
        new \Zend\Db\Sql\Predicate\Like($this->tableName . ".first_name", "%" . $filter['first_name_keyword'] . "%")
	    );
		}

		if(isset($filter['last_name_keyword']) && !empty($filter['last_name_keyword'])){
	    $where->addPredicate(
        new \Zend\Db\Sql\Predicate\Like($this->tableName . ".last_name", "%" . $filter['last_name_keyword'] . "%")
	    );
		}

    if (!empty($where)) {
      $select->where($where);
    }

    if(count($order) > 0){
      $select->order($order);
    }

    // echo $select->getSqlString($this->dbAdapter->getPlatform()) . "<br>";

    if($paginated) {
      $paginatorAdapter = new DbSelect(
        $select,
        $this->dbAdapter
      );
      $paginator = new Paginator($paginatorAdapter);
      return $paginator;
    }else{
      $statement = $this->sql->prepareStatementForSqlObject($select);
      $result = $statement->execute();
      if ($result instanceof ResultInterface && $result->isQueryResult()) {
        $resultSet = new ResultSet;
        $resultSet->initialize($result);
      }
    }

    return $resultSet;
	}

	public function getOfws($paginated=false, $filter = array(), $order = array())
	{
    $select = $this->sql->select();
    $select->columns(array(
      'id',
      'first_name',
			'middle_name',
      'last_name',
      'profile_pic',
      'last_login_datetime',
			'mobile_no',
    ));
		$select->join(
      'user_location',
      $this->tableName . ".id = user_location.user_id",
      array(
        'latitude',
				'longitude',
				'country_code',
				'country_name',
				'city',
				'ip',
				/*
				'distance' => new \Zend\Db\Sql\Expression("SQRT(POW(69.1 * (user_location.latitude - " . $filter['latitude'] . "), 2)
						+ POW(69.1 * (" . $filter['longitude'] . " - user_location.longitude) * COS(user_location.latitude / 57.3), 2))"),
				*/
				// Note: The provided distance is in Miles. If you need Kilometers, use 6371 instead of 3959.
				'distance' => new \Zend\Db\Sql\Expression("( 6371 * acos( cos( radians(" . $filter['latitude'] . ") ) * cos( radians( user_location.latitude ) )
				   * cos( radians(user_location.longitude) - radians(" . $filter['longitude'] . ")) + sin(radians(" . $filter['latitude'] . "))
				   * sin( radians(user_location.latitude))))"),
      ),
      $select::JOIN_INNER
    );

    $where = new \Zend\Db\Sql\Where();

    if(isset($filter['id'])){
			$where->equalTo($this->tableName . ".id", $filter['id']);
		}

		if(isset($filter['id_not'])){
	    $where->notEqualTo($this->tableName . ".id", $filter['id_not']);
		}

		if(isset($filter['user_id']) && !empty($filter['user_id'])){
			$where->equalTo($this->tableName . ".id", $filter['user_id']);
		}

		if(isset($filter['role']) && !empty($filter['role'])){
	    $where->equalTo("role", $filter['role']);
		}

		if(isset($filter['first_name_keyword']) && !empty($filter['first_name_keyword'])){
	    $where->addPredicate(
        new \Zend\Db\Sql\Predicate\Like($this->tableName . ".first_name", "%" . $filter['first_name_keyword'] . "%")
	    );
		}

		if(isset($filter['last_name_keyword']) && !empty($filter['last_name_keyword'])){
	    $where->addPredicate(
        new \Zend\Db\Sql\Predicate\Like($this->tableName . ".last_name", "%" . $filter['last_name_keyword'] . "%")
	    );
		}

    if (!empty($where)) {
      $select->where($where);
    }

    if(count($order) > 0){
      $select->order($order);
    }

    // echo $select->getSqlString($this->dbAdapter->getPlatform()) . "<br>";

    if($paginated) {
      $paginatorAdapter = new DbSelect(
        $select,
        $this->dbAdapter
      );
      $paginator = new Paginator($paginatorAdapter);
      return $paginator;
    }else{
      $statement = $this->sql->prepareStatementForSqlObject($select);
      $result = $statement->execute();
      if ($result instanceof ResultInterface && $result->isQueryResult()) {
        $resultSet = new ResultSet;
        $resultSet->initialize($result);
      }
    }

    return $resultSet;
	}
}

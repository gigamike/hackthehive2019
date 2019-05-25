<?php
namespace Language\Model;

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
use Language\Model\LanguageEntity;

class LanguageMapper
{
	protected $tableName = 'language';
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
		
		if(isset($filter['code'])){
		    $where->equalTo("code", $filter['code']);
		}
		
		if(isset($filter['language_keyword'])){
			$where->addPredicate(
					new \Zend\Db\Sql\Predicate\Like("language_keyword", "%" . $filter['language_keyword'] . "%")
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
		    $entityPrototype = new LanguageEntity();
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
		    
		    $entityPrototype = new LanguageEntity();
		    $hydrator = new ClassMethods();
		    $resultset = new HydratingResultSet($hydrator, $entityPrototype);
		    $resultset->initialize($results);
		}
		
		return $resultset;
	}
	
	public function saveLanguage(LanguageEntity $language)
	{
		$hydrator = new ClassMethods();
		$data = $hydrator->extract($language);
	
		if ($language->getId()) {
			// update action
			$action = $this->sql->update();
			$action->set($data);
			$action->where(array('id' => $language->getId()));
		} else {
			// insert action
			$action = $this->sql->insert();
			unset($data['id']);
			$action->values($data);
		}
		$statement = $this->sql->prepareStatementForSqlObject($action);
		$result = $statement->execute();
	
		if (!$language->getId()) {
			$language->setId($result->getGeneratedValue());
		}
		return $result;
	}
	
	public function getLanguage($id)
	{
		$select = $this->sql->select();
		$select->where(array('id' => $id));
	
		$statement = $this->sql->prepareStatementForSqlObject($select);
		$result = $statement->execute()->current();
		if (!$result) {
			return null;
		}
	
		$hydrator = new ClassMethods();
		$language = new LanguageEntity();
		$hydrator->hydrate($result, $language);
	
		return $language;
	}
	
	public function deleteLanguage($id)
	{
	    $delete = $this->sql->delete();
	    $delete->where(array('id' => $id));
	
	    $statement = $this->sql->prepareStatementForSqlObject($delete);
	    return $statement->execute();
	}
}
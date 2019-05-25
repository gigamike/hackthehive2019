<?php
return array(
	'controllers' => array(
		'invokables' => array(
			'User\Controller\Index' => 'User\Controller\IndexController',
			'User\Controller\Auth' => 'User\Controller\AuthController',
			'User\Controller\Community' => 'User\Controller\CommunityController',
			'User\Controller\Ofw' => 'User\Controller\OfwController',
		),
	),
	'view_manager' => array(
		'template_path_stack' => array(
			'user' => __DIR__ . '/../view',
		),
	),
);

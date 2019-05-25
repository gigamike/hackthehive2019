<?php
return array(
	'controllers' => array(
		'invokables' => array(
			'Volunteer\Controller\Index' => 'Volunteer\Controller\IndexController',
		),
	),
	'view_manager' => array(
		'template_path_stack' => array(
			'volunteer' => __DIR__ . '/../view',
		),
	),
);

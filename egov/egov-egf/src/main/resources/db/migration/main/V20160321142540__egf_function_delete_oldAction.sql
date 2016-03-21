

delete from eg_roleaction where actionid =(select id from eg_action where name='Function-Create');
delete from eg_roleaction where actionid =(select id from eg_action where name='Function');
delete from eg_roleaction where actionid =(select id from eg_action where name='Function-BeforeModify');
delete from eg_roleaction where actionid =(select id from eg_action where name='Function-View');
delete from eg_roleaction where actionid =(select id from eg_action where name='Function-Modify');
delete from eg_roleaction where actionid =(select id from eg_action where name='FunctionSearch');
delete from eg_roleaction where actionid =(select id from eg_action where name='FunctionEdit');
delete from eg_roleaction where actionid =(select id from eg_action where name='FunctionCreate');

delete from eg_action where name='Function-Create';
delete from eg_action where name='Function';
delete from eg_action where name='Function-BeforeModify';
delete from eg_action where name='Function-View';
delete from eg_action where name='Function-Modify';
delete from eg_action where name='FunctionSearch';
delete from eg_action where name='FunctionEdit';
delete from eg_action where name='FunctionCreate';

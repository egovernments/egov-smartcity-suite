insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Add/Modify Bank Account Cheque'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Ajax-load Banks'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-loadbaccount'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='accountCheque-manipulateCheques'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='AccountChequeSave'));


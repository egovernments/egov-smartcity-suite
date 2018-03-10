
delete from eg_roleaction  where roleid  =(select id from eg_role  where name  ='TLApprover') and 
actionid =(select id from eg_action where name='AjaxDesignationsByDepartmentWithDesignation');

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='AjaxDesignationsByDepartmentWithDesignation'),
(select id from eg_role  where name  ='TLApprover'));

delete from eg_roleaction  where roleid  =(select id from eg_role  where name  ='TLApprover') and 
actionid =(select id from eg_action where name='AjaxApproverByDesignationAndDepartment');

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='AjaxApproverByDesignationAndDepartment'),
(select id from eg_role  where name  ='TLApprover'));

delete from eg_roleaction  where roleid  =(select id from eg_role  where name  ='CITIZEN') and 
actionid =(select id from eg_action where name='View Support Documents');

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_role  where name  ='CITIZEN'));
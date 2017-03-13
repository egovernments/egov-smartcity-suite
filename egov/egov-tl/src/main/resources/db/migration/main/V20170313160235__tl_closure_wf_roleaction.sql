insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='viewclosurelicense'));

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='updateclosurelicense'));

delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxDesignationDropdown') and roleid=(select id from eg_role where name ='CSC Operator');

delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxApproverDropdown') and roleid=(select id from eg_role where name ='CSC Operator');

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='AjaxDesignationDropdown'));

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='AjaxApproverDropdown'));


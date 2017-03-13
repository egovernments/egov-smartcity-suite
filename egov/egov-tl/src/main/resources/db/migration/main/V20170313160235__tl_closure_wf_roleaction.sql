insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='viewclosurelicense'));

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='updateclosurelicense'));

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='AjaxDesignationDropdown'));

insert into eg_roleaction values ((select id from eg_role where name ='CSC Operator'),(select id from eg_action where name='AjaxApproverDropdown'));


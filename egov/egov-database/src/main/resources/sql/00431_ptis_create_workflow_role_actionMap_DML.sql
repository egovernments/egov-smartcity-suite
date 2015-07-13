insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxDesignationDropdown') from eg_role where name='ULB Operator');

insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxApproverDropdown') from eg_role where name='ULB Operator');


insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxDesignationDropdown') from eg_role where name='Property Approver');

insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxApproverDropdown') from eg_role where name='Property Approver');


insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxDesignationDropdown') from eg_role where name='Property Verifier');

insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxApproverDropdown') from eg_role where name='Property Verifier');



--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxDesignationDropdown') and roleid=(Select id from eg_role where name='Property Approver');

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxApproverDropdown') and roleid=(Select id from eg_role where name='Property Approver');

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxDesignationDropdown') and roleid=(Select id from eg_role where name='ULB Operator');

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxApproverDropdown') and roleid=(Select id from eg_role where name='ULB Operator');


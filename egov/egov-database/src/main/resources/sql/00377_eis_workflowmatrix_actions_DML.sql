INSERT INTO EG_ACTION 
	(ID,NAME,createddate,URL,QUERYPARAMS,parentmodule,ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT ) 
VALUES 
	(nextval('SEQ_EG_ACTION'), 'AjaxDesignationDropdown',current_date , 
'/workflow/ajaxWorkFlow-getDesignationsByObjectType.action', NULL,  (select ID from eg_module where name like 'Workflow'), 6, 'AjaxDesignationDropdown', false, 'eis');



INSERT INTO EG_ACTION 
	(ID,NAME,createddate,URL,QUERYPARAMS,parentmodule,ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT ) 
VALUES 
	(nextval('SEQ_EG_ACTION'), 'AjaxApproverDropdown',current_date , 
'/workflow/ajaxWorkFlow-getPositionByPassingDesigId.action', NULL,  (select ID from eg_module where name like 'Workflow'), 6, 'AjaxApproverDropdown', false, 'eis');


insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxDesignationDropdown') from eg_role where name='SuperUser');

insert into eg_roleaction
(Select id,(select id from eg_action where name='AjaxApproverDropdown') from eg_role where name='SuperUser');

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxDesignationDropdown') and roleid=(Select id from eg_role where name='SuperUser');

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AjaxApproverDropdown') and roleid=(Select id from eg_role where name='SuperUser');

--delete from eg_action where name='AjaxApproverDropdown';
--delete from eg_action where name='AjaxDesignationDropdown';

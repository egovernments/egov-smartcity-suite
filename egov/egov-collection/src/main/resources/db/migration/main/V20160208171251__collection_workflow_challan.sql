delete from eg_roleaction where roleid in (select id from eg_role where name = 'ULB Operator') and actionid in (select id from eg_action where name in('CreateChallan','AjaxChallanApproverDesignation','AjaxChallanApproverPosition'));

INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version)
VALUES (nextval('SEQ_EG_ROLE'), 'Challan Creator', 'Challan Creator',  now(), 1, 1,  now(), 0);
 
INSERT INTO EG_USERROLE(ROLEID,USERID)(SELECT (SELECT id FROM eg_role WHERE name = 'Challan Creator'),u.id  
FROM view_egeis_employee v ,eg_user u WHERE u.username  = v.username
AND v.designation in (SELECT id from eg_designation where name in ('Junior Assistant')) 
and v.department in (select id from eg_department where upper(name) in ('REVENUE','ACCOUNTS','ADMINISTRATION')));

Insert into eg_roleaction (roleid, actionid) (select (select id from eg_role where name = 'Challan Creator')  as roleid , id from eg_action where name in('CreateChallan','AjaxChallanApproverDesignation','AjaxChallanApproverPosition','SaveChallan'));

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDESIGCHALLANWORKFLOW','Collection Challan Validator Designation',0, (select id from eg_module where name='Collection')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDESIGCHALLANWORKFLOW'),current_date, 'Senior Assistant',0);

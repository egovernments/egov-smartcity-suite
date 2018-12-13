INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'EIS_POSITIONS_BY_DEPT_DESIG','/position/by-dept-desig',
 (SELECT id FROM eg_module WHERE name='Position'),0,'EIS_POSITIONS_BY_DEPT_DESIG',false,
 'eis',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='EIS'));

INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'EIS_DESIGNATION_BY_DEPARTMENT','/designation/by-department',
 (SELECT id FROM eg_module WHERE name='Designation'),0,'EIS_DESIGNATION_BY_DEPARTMENT',false,
 'eis',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='EIS'));
 
INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'EIS_DESIGNATION_BY_NAME','/designation/by-name',
 (SELECT id FROM eg_module WHERE name='Designation'),0,'EIS_DESIGNATION_BY_NAME',false,
 'eis',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='EIS'));

INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'EIS_POSITIONS_BY_CODE_NAME_POSITION_NAME','/employee/position/by-code-or-name-or-position',
 (SELECT id FROM eg_module WHERE name='Position'),0,'EIS_POSITIONS_BY_CODE_NAME_POSITION_NAME',false,
 'eis',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='EIS'));


-- Default functioncode
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SEWERAGE_FUNCTION_CODE', 'Function Code for Collections',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGE_FUNCTION_CODE' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, '5052',0);

-- Collect inspection fee default flag
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SEWERAGE_COLLECTINSPECTION_FEE', 'Collect Inspection Fee',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGE_COLLECTINSPECTION_FEE' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, 'YES',0);

-- Update demand reason with glcode

update EG_DEMAND_REASON  set glcodeid=(select id from chartofaccounts where glcode='1404019')   where ID_DEMAND_REASON_MASTER in (select id from eg_demand_reason_master where reasonmaster in ('Inspection Charge','Estimation Charge','Sewerage Tax')
 and module=(select id from eg_module where name='Sewerage Tax Management'));

update EG_DEMAND_REASON  set glcodeid=(select id from chartofaccounts where glcode='1603006')  where ID_DEMAND_REASON_MASTER in (select id from eg_demand_reason_master where reasonmaster in ('Donation Charge')
 and module=(select id from eg_module where name='Sewerage Tax Management'));

-- New status as collection inspection fee

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','COLLECTINSPECTIONFEE',now(),'COLLECTINSPECTIONFEE',12);



INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'generateSwtBillForCollection', '/collection/generatebill', NULL, (select id from eg_module where name='Sewerage Tax Management'), 1, 'generateSwtBillForCollection', false, 'stms', 0, 1,now(), 1, now(), (select id from eg_module where name='Sewerage Tax Management' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'generateSwtBillForCollection'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='generateSwtBillForCollection'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchSewerageConnection','/existing/sewerage',null,(select id from EG_MODULE where name = 'SewerageTransactions'),1,'Search Connection',true,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Collection Operator'),(select id from eg_action where name ='SearchSewerageConnection' and contextroot = 'stms'));
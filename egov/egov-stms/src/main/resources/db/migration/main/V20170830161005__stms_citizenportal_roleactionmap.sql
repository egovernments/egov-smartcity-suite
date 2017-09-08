INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='New Sewerage Connection'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='AjaxCheckConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION(roleid, actionid)  values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name = 'AjaxCheckWaterTaxDue'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='Create Sewerage Connection' and contextroot = 'stms'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='SewerageCitizenOnlineBill' and contextroot = 'stms'));



INSERT INTO eg_roleaction (actionid,roleid) values((select id from eg_action where name ='SewerageChangeInClosets'),(select id from eg_role where name='CITIZEN'));
INSERT INTO eg_roleaction (actionid,roleid) values((select id from eg_action where name ='SewerageConnectionChangeInClosetsValidation'),(select id from eg_role where name='CITIZEN'));


--------------------------------workflow matrix for citizen----------------------
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Citizen created', NULL, NULL, NULL, 'NEWSEWERAGECONNECTION', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CITIZENCREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');

-------------------------------create new status for citizen-------------------------------------------------------

INSERT INTO egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','CITIZEN Created',now(),'CITIZENCREATED',17);
INSERT INTO egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','citizen fee collection pending',now(),'FEECOLLECTIONPENDING',18);


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGEROLEFORNONEMPLOYEE'),current_date, 'CITIZEN',0);

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions,fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Fee collection Pending', NULL, NULL, NULL, 'CHANGEINCLOSETS', 'NEW', 'Junior/Senior Assistance approval pending',NULL, 'NEW','Forward', NULL, NULL, '2017-01-01', '2099-04-01');
 
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Citizen created', NULL, NULL, NULL, 'CHANGEINCLOSETS', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CITIZENCREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');
 
update eg_wf_matrix set nextstate ='Fee collection Pending' where currentstate='Citizen created' and additionalrule='CHANGEINCLOSETS';

 
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'CitizenUpdateSewerageChangeInClosets','/transactions/citizenmodifyConnection-update/',null,(select id from eg_module where name='SewerageTransactions'),null,'Citizen Update Sewerage change in closets','false','stms',0,1,to_timestamp('2015-08-15 11:04:04.11703','null'),1,to_timestamp('2015-08-15 11:04:04.11703','null'),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CitizenUpdateSewerageChangeInClosets'),(select id from eg_role where name='CITIZEN'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'CitizenUpdateSewerageApplicationDetails','/transactions/citizenupdate/',null,(select id from eg_module where name='SewerageTransactions'),null,'Citizen Update Sewerage Application Details','false','stms',0,1,to_timestamp('2015-08-15 11:04:04.11703','null'),1,to_timestamp('2015-08-15 11:04:04.11703','null'),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CitizenUpdateSewerageApplicationDetails'),(select id from eg_role where name='CITIZEN'));
--------add source column ---------------------
alter table egswtax_applicationdetails add column source varchar(20) default 'SYSTEM' not null;

----------------------------create SEWERAGE roleaction for CSC operator------------------

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name ='New Sewerage Connection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name ='AjaxCheckConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION(roleid, actionid)  values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name = 'AjaxCheckWaterTaxDue'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name ='Create Sewerage Connection' and contextroot = 'stms'));


--------------------------------workflow matrix for CSC Operator----------------------
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Third Party operator created', NULL, NULL, NULL, 'NEWSEWERAGECONNECTION', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CSCCREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');

-------------------------------create new status for CSC Opertaor-----

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','CSC Created',now(),'CSCCREATED',15);


----------------------------- app config value to identify Non-Employee for adtax-----------------
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SEWERAGEROLEFORNONEMPLOYEE', 'roles for sewerage tax workflow',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGEROLEFORNONEMPLOYEE'),current_date, 'CSC Operator',0);

------------------------------appconfig value for to retrieve CSC operator designation --------------------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'SEWERAGEDESIGNATIONFORCSCOPERATORWORKFLOW', 'Designation for Csc Workflow',0, (select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGEDESIGNATIONFORCSCOPERATORWORKFLOW'), current_date, 'Junior Assistant,Senior Assistant',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SEWERAGEDEPARTMENTFORCSCOPERATORWORKFLOW', 'Department for Csc Workflow',0, (select id from eg_module where name='Advertisement Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGEDEPARTMENTFORCSCOPERATORWORKFLOW'), current_date, 'Town Planning,Revenue,Accounts,Administration',0);


-------------------print Acknowledgement-----------------


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage Acknowledgement', '/transactions/new-sewerage-ackowledgement', null, (select id from eg_module where name = 'SewerageTransactions'), 1, 'Sewerage acknowledgement', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'Sewerage Acknowledgement'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Print Sewerage Acknowledgement', '/transactions/printacknowledgement', null, (select id from eg_module where name = 'SewerageTransactions'), 1, 'Print Sewerage acknowledgement', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'Print Sewerage Acknowledgement'));
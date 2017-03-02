----------------------------- app config value to identify Non-Employee for MRS-----------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MRSROLEFORNONEMPLOYEE', 'roles for marriage registration workflow',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSROLEFORNONEMPLOYEE'),current_date, 'CSC Operator',0);

--------------------------------workflow matrix for CSC Operator----------------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'CSC Operator created', NULL, NULL, NULL, 'MARRIAGE REGISTRATION', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'CSC Operator created', NULL, NULL, NULL, 'MARRIAGE REGISTRATION', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');

------------------------------appconfig value for to retrieve CSC operator desig --------------------------
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'MRSDESIGNATIONFORCSCOPERATORWORKFLOW', 'Designation for Csc Workflow',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDESIGNATIONFORCSCOPERATORWORKFLOW'), current_date, 'Junior Assistant,Senior Assistant',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MRSDEPARTMENTFORCSCOPERATORWORKFLOW', 'Department for Csc Workflow',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDEPARTMENTFORCSCOPERATORWORKFLOW'), current_date, 'Administration',0);



----------------------------create registration and reissue roleaction for CSC operator------------------



INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'CreateRegistration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'calculateMarriageFee'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Re Issue Marriage Certifiate'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'CreateReIssue'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Search register status MR records'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'show-mrregistrationunitzone'));


-------------------------------New Marriage Registration acknowledgement role action mapping---------------

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'Show Acknowledgement For Marriage Registration', '/registration/new-mrgregistration-ackowledgement', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 8, 'Show Acknowledgement For Marriage Registration', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Show Acknowledgement For Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Show Acknowledgement For Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Show Acknowledgement For Marriage Registration') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'New Marriage Registration Print Acknowledgement', '/registration/printmarriageregistrationack', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 9, 'New Marriage Registration Print Acknowledgement', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'New Marriage Registration Print Acknowledgement'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'New Marriage Registration Print Acknowledgement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New Marriage Registration Print Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name ='Create Marriage Registration'));

-------------------------------Reissue certificate acknowledgement role action mapping-----------------------------

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'Reissue Marriage Certificate Show Acknowledgement', '/reissue/reissue-certificate-ackowledgement', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 10, 'Reissue Marriage Certificate Show Acknowledgement', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Reissue Marriage Certificate Show Acknowledgement'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Reissue Marriage Certificate Show Acknowledgement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Reissue Marriage Certificate Show Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'Reissue Marriage Certificate Print Acknowledgement', '/reissue/printreissuecertificateack', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 11, 'Reissue Marriage Certificate Print Acknowledgement', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Reissue Marriage Certificate Print Acknowledgement'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Reissue Marriage Certificate Print Acknowledgement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Reissue Marriage Certificate Print Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name ='Create Reissue'));
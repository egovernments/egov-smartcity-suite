INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Anonymous  Created', NULL, NULL, NULL, 'NEWSEWERAGECONNECTION', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'ANONYMOUSCREATED','Forward', NULL, NULL, '2017-01-01', '2099-04-01');

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','Anonymous Created',now(),'ANONYMOUSCREATED',16);

-------New sewerage feature action --------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Print Sewerage Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));

-------New sewerage application feature role--
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGEROLEFORNONEMPLOYEE'),current_date, 'PUBLIC',0);
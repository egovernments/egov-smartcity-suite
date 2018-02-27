
update eg_action set url = '/application/regulariseconnection-form/' where name='WaterTaxCreateRegularisedConnectionNewForm' and contextroot='wtms';

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'UpdateWaterConnExecutionDetails', '/application/execute-update/update', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'Update Water Connection Details', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='UpdateWaterConnExecutionDetails'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='UpdateWaterConnExecutionDetails'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='UpdateWaterConnExecutionDetails'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewRegulariseConnDemandNote', '/application/regulariseconnection/demandnote-view', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'View Regularise Connection Demand Note', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewRegulariseConnDemandNote'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='ViewRegulariseConnDemandNote'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='ViewRegulariseConnDemandNote'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewRegulariseConnProceedings', '/application/regulariseconnection/proceedings-view', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'View Regularise Connection Proceedings', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewRegulariseConnProceedings'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='ViewRegulariseConnProceedings'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='ViewRegulariseConnProceedings'));


Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'WCMS_SERVICE_CHARGES','Wcms Regularise Connection service charges',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='WCMS_SERVICE_CHARGES' and module=(select id from eg_module where name='Water Tax Management')), now(), '300', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Water Tax Approver'),(select id FROM eg_action 
 WHERE NAME = 'editdataEntryDemand' and CONTEXTROOT='wtms'));


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'NEW', 'NULL', '', '', 'REGLZNCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer,Assistant Executive Engineer,Tap Inspector', 'Clerk approved', 'Forward, Reject', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Clerk approved', 'NULL', 'Asst.engineer approval pending', 'Assistant Engineer', 'REGLZNCONNECTION', 'Assistant Engineer approved', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'Assistant Engineer approved', 'Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Clerk approved', 'NULL', 'Assistant Executive Engineer approval pending', 'Assistant Executive Engineer', 'REGLZNCONNECTION', 'Assistant Executive Engineer approved', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'Assistant Executive Engineer approved', 'Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Clerk approved', 'NULL', 'Tap Inspector approval pending', 'Tap Inspector', 'REGLZNCONNECTION', 'Tap Inspector approved', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'Tap Inspector approved', 'Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Assistant Engineer approved', 'NULL', 'Deputy Executive Engineer forward pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer forwarded', 'Executive Engineer approval pending', 'Executive Engineer', 'Deputy Executive Engineer forwarded', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Assistant Executive Engineer approved', 'NULL', 'Deputy Executive Engineer forward pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer forwarded', 'Executive Engineer approval pending', 'Executive Engineer', 'Deputy Executive Engineer forwarded', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Tap Inspector approved', 'NULL', 'Deputy Executive Engineer forward pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer forwarded', 'Executive Engineer approval pending', 'Executive Engineer', 'Deputy Executive Engineer forwarded', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer forwarded', 'NULL', 'Executive Engineer approval pending', 'Executive Engineer', 'REGLZNCONNECTION', 'Executive Engineer approved', 'Digital Signature pending', 'Superintending Engineer', 'Executive Engineer approved', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Executive Engineer forwarded', 'NULL', 'Superintending Engineer approval pending', 'Superintending Engineer', 'REGLZNCONNECTION', 'Superintending Engineer approved', 'Digital Signature pending', 'Municipal Engineer', 'Superintending Engineer approved', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Superintending Engineer forwarded', 'NULL', 'Municipal Engineer approval pending', 'Municipal Engineer', 'REGLZNCONNECTION', 'Municipal Engineer approved', 'Digital Signature pending', 'Commissioner', 'Municipal Engineer approved', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Assistant Engineer approved', 'NULL', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer approved', 'Digital Signature pending', 'Commissioner', 'Deputy Executive Engineer approved', 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Assistant Executive Engineer approved', 'NULL', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer approved', 'Digital Signature pending', 'Commissioner', 'Deputy Executive Engineer approved', 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Tap Inspector approved', 'NULL', 'Deputy Executive Engineer approval pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Deputy Executive Engineer approved', 'Digital Signature pending', 'Commissioner', 'Deputy Executive Engineer approved', 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer forwarded', 'NULL', 'Executive Engineer forward pending', 'Executive Engineer', 'REGLZNCONNECTION', 'Executive Engineer forwarded', 'Superintending Engineer approval pending', 'Superintending Engineer', 'Executive Engineer forwarded', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Executive Engineer forwarded', 'NULL', 'Superintending Engineer forward pending', 'Superintending Engineer', 'REGLZNCONNECTION', 'Superintending Engineer forwarded', 'Municipal Engineer approval pending', 'Municipal Engineer', 'Superintending Engineer forwarded', 'Approve,Forward', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Superintending Engineer forwarded', 'NULL', 'Municipal Engineer forward pending', 'Municipal Engineer', 'REGLZNCONNECTION', 'Municipal Engineer forwarded', 'Commissioner approval pending', 'Commissioner', 'Municipal Engineer forwarded', 'Approve', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Municipal Engineer forwarded', 'NULL', 'Commissioner approval pending', 'Commissioner', 'REGLZNCONNECTION', 'Commissioner approved', 'Commissioner Digisign pending', 'Commissioner', 'Commissioner approved', 'Approve', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Commissioner approved', 'NULL', 'Commissioner Digisign pending', 'Commissioner', 'REGLZNCONNECTION', 'Commissioner Digital Signature Updated', 'END', null, null, 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approved', 'NULL', 'Digital Signature pending', 'Deputy Executive Engineer', 'REGLZNCONNECTION', 'Digital Signature Updated', 'END', null, null, 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Executive Engineer approved', 'NULL', 'Digital Signature pending', 'Executive Engineer', 'REGLZNCONNECTION', 'Digital Signature Updated', 'END', null, null, 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Superintending Engineer approved', 'NULL', 'Digital Signature pending', 'Superintending Engineer', 'REGLZNCONNECTION', 'Digital Signature Updated', 'END', null, null, 'Sign', null, null, '2016-04-01', '2099-04-01');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Municipal Engineer approved', 'NULL', 'Digital Signature pending', 'Municipal Engineer', 'REGLZNCONNECTION', 'Digital Signature Updated', 'END', null, null, 'Sign', null, null, '2016-04-01', '2099-04-01');

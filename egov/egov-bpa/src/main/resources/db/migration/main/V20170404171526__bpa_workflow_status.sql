INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version)
 VALUES (nextval('seq_eg_wf_types'), (select id from eg_module where name='BPA'), 'BpaApplication', '/bpa/application/update/:ID', 1, now(), 1, now(), 'Y', 'N', 'org.egov.bpa.application.entity.BpaApplication', 'Building Plan Application', 0);

 
------- BPA status ---

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Registered',now(),'Registered',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Document Verified',now(),'Document Verified',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Field Inspected',now(),'Field Inspected',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Approved',now(),'Approved',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Order Issued to Applicant',now(),'Order Issued to Applicant',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Digitally signed',now(),'Digitally signed',true,0,1,now());

Insert into EGBPA_STATUS (ID,MODULETYPE,description,LASTMODIFIEDDATE,CODE,isactive,version,createdby,createddate)
 values (nextval('SEQ_EGBPA_STATUS'),'REGISTRATION','Cancelled',now(),'Cancelled',true,0,1,now());

 
 

--- Workflow configured

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'NEW', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Registered', 'Forwarded to Superintendent', 'Superintendent', 'Registered', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Registered', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Superintendent Approved', 'Document verification pending', 'Senior Assistant,Junior Assistant', 'Registered', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Superintendent Approved', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Document Verified', 'Forwarded to Superintendent-Noc', 'Superintendent', 'Document Verified', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Document Verified', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Initiate Noc', 'Forwarded to Assistant Engineer for field ispection', 'Assistant Engineer', 'Document Verified', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Initiate Noc', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Field Inspected', 'Forwarded to Superintendent for Noc Updation', 'Superintendent', 'Field Inspected', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Field Inspected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Noc Details Updated', 'Forwarded to Approval', 'Superintendent', 'Field Inspected', 'Forward',0, 299, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Field Inspected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Noc Details Updated', 'Forwarded to Approval', 'Assistant Engineer', 'Field Inspected', 'Forward',300, 749, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Field Inspected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Noc Details Updated', 'Forwarded to Approval', 'Executive Engineer', 'Field Inspected', 'Forward',750, 1499, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Field Inspected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Noc Details Updated', 'Forwarded to Approval', 'Chief Engineer', 'Field Inspected', 'Forward',1500, 9999, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Field Inspected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Noc Details Updated', 'Forwarded to Approval', 'Secretary', 'Field Inspected', 'Forward',10000, 1000000, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Noc Details Updated', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Record Approved', 'Forwarded to Digital Signature', 'Assistant Engineer', 'Approved', 'Approve',0, 299, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Noc Details Updated', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Record Approved', 'Forwarded to Digital Signature', 'Assistant Executive Engineer', 'Approved', 'Approve',300, 749, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Noc Details Updated', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Record Approved', 'Forwarded to Digital Signature', 'Executive Engineer', 'Approved', 'Approve',750, 1499, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Noc Details Updated', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Record Approved', 'Forwarded to Digital Signature', 'Chief Engineer', 'Approved', 'Approve',1500, 9999, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Noc Details Updated', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Record Approved', 'Forwarded to Digital Signature', 'Secretary', 'Approved', 'Approve',10000, 1000000, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Record Approved', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Digitally signed', 'Forwarded to generate permit order', 'Superintendent', 'Digitally signed', 'Sign',null, null, '2017-01-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Digitally signed', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'END', 'Generated permit orders', 'Superintendent', 'Order Issued to Applicant', 'Close Registration',null,null, '2017-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Rejected', NULL, NULL, null, 'CREATEBPAAPPLICATION', 'Superintendent Approved', 'Document verification pending', 'Senior Assistant,Junior Assistant', '', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

 
ALTER TABLE EGBPA_SITEDETAIL RENAME COLUMN nearestbuildingnumeric TO nearestbuildingnumber;
alter table EGBPA_SITEDETAIL add column subdivisionnumber character varying(128) ;
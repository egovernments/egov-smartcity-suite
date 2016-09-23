--------------------egswtax_application_type--------------------
INSERT INTO egswtax_application_type(id, code, name, description, processingtime, active, createddate, lastmodifieddate, createdby, lastmodifiedby, version) VALUES (nextval('seq_egswtax_application_type'), 'CHANGEINCLOSETS', 'Change In Closets', null, 1, true, now(), now(), 4, 4, 0);

--------------------EG_ACTION-------------------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SewerageChangeInClosets','/transactions/modifyConnection/',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Change In Closets','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='SewerageChangeInClosets' and contextroot = 'stms'));

--------------------EG_WF_MATRIX-------------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','NEW', NULL, NULL,'Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Clerk Approved','Assistant Engineer Approval Pending','Assistant Engineer','CREATED','Forward', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Clerk Approved',NULL,'Assistant Engineer Approval Pending','Assistant Engineer','CHANGEINCLOSETS','Assistant Engineer Approved','Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','INITIALAPPROVED','Forward,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Rejected',NULL,'Rejected','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Clerk Approved','Assistant Engineer Approval Pending','Assistant Engineer','CREATED','Forward,Cancel', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','NEW',NULL,'Inspection Fee Collection','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Inspection Fee Pending','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPENDING','Forward', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Inspection Fee Pending',NULL,'Assistant Engineer Approval Pending','Assistant Engineer','CHANGEINCLOSETS','Inspection Fee Collected','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPAID','Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Inspection Fee Collected',NULL,'Assistant Engineer Approval Pending','Assistant Engineer','CHANGEINCLOSETS','Assistant Engineer Approved','Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','INITIALAPPROVED','Forward,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Rejected',NULL,'Rejected Inspection Fee Collection','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Inspection Fee Collected','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPAID','Forward,Cancel', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Assistant Engineer Approved',NULL,'Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','CHANGEINCLOSETS','Deputy Exe Engineer Approved','Estimation Notice Generation Pending','Senior Assistant,Junior Assistant','DEEAPPROVED','Approve,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Exe Engineer Approved',NULL,'Estimation Notice Generation Pending','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Estimation Notice Generated','Donation Charges Payment Pending','Senior Assistant,Junior Assistant','ESTIMATIONAMOUNTPAID ','Generate Estimation Notice', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Estimation Notice Generated',NULL,'Donation Charges Payment Pending','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Payment Done Against Estimation','Executive Engineer Approval Pending','Executive Engineer','ESTIMATIONAMOUNTPAID','Forward,Cancel', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Payment Done Against Estimation',NULL,'Executive Engineer Approval Pending','Executive Engineer','CHANGEINCLOSETS','Executive Engineer Approved','Work Order Generation Pending','Senior Assistant,Junior Assistant','FINALAPPROVED','Approve', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Executive Engineer Approved',NULL,'Work Order Generation Pending','Senior Assistant,Junior Assistant','CHANGEINCLOSETS','Work Order Generated','Connection Execution Pending','Assistant Engineer','WORKORDERGENERATED','Forward', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Work Order Generated',NULL,'Connection Execution Pending','Assistant Engineer','CHANGEINCLOSETS','END','END',NULL,NULL,'Execute Connection', NULL, NULL,now(),now(),'0');

------ START : Insert New status for workflow ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','New',now(),'NEW',0);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Created',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Approved',now(),'APPROVED',2);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Rejected',now(),'REJECTED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Cancelled',now(),'CANCELLED',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Re-Submitted',now(),'RESUBMITTED',5);
------ END : Insert New status for workflow ---

------ START : RevisionAbstractEstimate workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'NEW', NULL, NULL, 'Assistant engineer', NULL, 'Created', 'Pending Approval', 'Assistant Executive Engineer', 'Created', 'Save,Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Created', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Rejected', NULL, NULL, 'Assistant engineer', NULL, 'Resubmitted', 'Pending Approval', 'Assistant Executive Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Resubmitted', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());

------ END : RevisionAbstractEstimate workflow  ---

insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version)
values(nextval('seq_eg_wf_types'),(select id from eg_module where name ='Works Management'),'RevisionAbstractEstimate','/egworks/revisionestimate/update/:ID',1,now(),1,now(),'Y','N','org.egov.works.revisionestimate.entity.RevisionAbstractEstimate','Revision Estimate',0);


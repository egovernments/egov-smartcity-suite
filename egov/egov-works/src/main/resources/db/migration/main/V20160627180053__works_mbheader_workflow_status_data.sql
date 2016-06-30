------ START : MBHeader workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'MBHeader', 'NEW', NULL, NULL, 'Assistant engineer', NULL, 'Created', 'Pending Approval', 'Assistant Executive Engineer', 'Created', 'Save,Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'MBHeader', 'Created', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'MBHeader', 'Rejected', NULL, NULL, 'Assistant engineer', NULL, 'Created', 'Pending Approval', 'Assistant Executive Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());
------ END : MBHeader workflow  ---

------ START : Insert Application types for workflow ---
insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version)
values(nextval('seq_eg_wf_types'),(select id from eg_module where name ='Works Management'),'MBHeader','/egworks/measurementbook/update/:ID',1,now(),1,now(),'Y','N','org.egov.works.mb.entity.MBHeader','Measurement Book',0);
------ END : Insert Application types for workflow ---

------ START : Insert New status for workflow ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'MBHeader','New',now(),'NEW',0);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'MBHeader','Rejected',now(),'REJECTED',6);
------ END : Insert New status for workflow ---

--rollback delete from EGW_STATUS where MODULETYPE = 'MBHeader' and DESCRIPTION = 'Rejected';
--rollback delete from EGW_STATUS where MODULETYPE = 'MBHeader' and DESCRIPTION = 'New';
--rollback delete from EG_WF_TYPES where type = 'MBHeader' and module = (select id from eg_module where name ='Works Management');
--rollback delete from EG_WF_MATRIX where objecttype = 'MBHeader';

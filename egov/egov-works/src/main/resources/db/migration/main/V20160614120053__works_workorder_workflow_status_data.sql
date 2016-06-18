------ START : WorkOrder workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WorkOrder', 'NEW', NULL, NULL, 'Assistant engineer', NULL, 'Approve', 'Pending Approval', 'Assistant Executive Engineer', 'Approve', 'Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WorkOrder', 'Approve', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WorkOrder', 'Rejected', NULL, NULL, 'Assistant engineer', NULL, 'Approve', 'Pending Approval', 'Assistant Executive Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());
------ END : WorkOrder workflow  ---

------ START : Insert Application types for workflow ---
insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version)
values(nextval('seq_eg_wf_types'),(select id from eg_module where name ='Works Management'),'WorkOrder','/egworks/letterofacceptance/update/:ID',1,now(),1,now(),'Y','N','org.egov.works.workorder.entity.WorkOrder','Letter Of Acceptance',0);

------ END : Insert Application types for workflow ---

--rollback delete from EG_WF_TYPES where type = 'WorkOrder' and module = (select id from eg_module where name ='Works Management');
--rollback delete from EG_WF_MATRIX where objecttype = 'WorkOrder';

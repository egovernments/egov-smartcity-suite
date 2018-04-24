


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version)
 VALUES (nextval('seq_eg_wf_matrix'), 'ADMINISTRATION', 'EmployeeGrievance', 'In process', NULL, NULL, 
'Commissioner', null, 'Created', 'Commissioner Approval pending', 'Commissioner', 
'Created', 'Submit', NULL, NULL, now(), now(),0);

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version)
 VALUES (nextval('seq_eg_wf_matrix'), 'ADMINISTRATION', 'EmployeeGrievance', 'Created', NULL, NULL, 
'Commissioner', null, 'Approved', 'END', null, 
null, 'Submit', NULL, NULL, now(), now(),0);


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version)
 VALUES (nextval('seq_eg_wf_matrix'), 'ADMINISTRATION', 'EmployeeGrievance', 'Registered', NULL, NULL, 
'Commissioner', null, 'Created', 'Commissioner Approval pending', 'Commissioner', 
'Created', 'Submit', NULL, NULL, now(), now(),0);


------ START : Application types for workflow ---
INSERT INTO EG_WF_TYPES (id, module, type, link, createdby, createddate, lastmodifiedby,
 lastmodifieddate, typefqn, displayname, version,enabled,grouped) VALUES 
(nextval('seq_eg_wf_types'), (select id from eg_module where name = 'EIS'), 'EmployeeGrievance', 
'/eis/employeegrievance/edit/:ID', 1, now(), 1, now(), 'org.egov.eis.entity.EmployeeGrievance', 'Employee Grievance', 0,true,false);

---- END : Application types for workflow ---





-- Workflow configuration for council preamble
INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Council Management'), 'CouncilPreamble', '/council/councilpreamble/edit/:ID', 1, now(), 1, now(), 'Y', 'N', 'org.egov.council.entity.CouncilPreamble', 'CouncilPreamble', 0);

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'CouncilPreamble', 'NEW', NULL, NULL, 'Assistant engineer', null, 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2016-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'CouncilPreamble', 'Created', 'CREATED', 'Commissioner approval pending', 'Commissioner', null, 'END', 'END', '', 'APPROVED', 'Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'CouncilPreamble', 'Rejected', 'REJECTED', NULL, 'Assistant engineer', null, 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

-- New Roles for council management

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Council Management Creator', 'Council Management Creator', now(), 1, 1, now(), 0);
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Council Management Approver', 'Council Management Approver', now(), 1, 1, now(), 0);
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Council Management Report viewer', 'Council Management Report viewer', now(), 1, 1, now(), 0);

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name='Edit-CouncilPreamble'));
INSERT into eg_roleaction values((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name='Update Preamble'));
INSERT into eg_roleaction values((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name='Result-CouncilPreamble'));
insert into eg_roleaction values ((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name='AjaxDesignationsByDepartment'));
insert into eg_roleaction values ((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));

insert into eg_roleaction (roleid,actionid) select (select id from eg_role where name='Council Management Creator'),id  from eg_action where name in ('New-CouncilPreamble','Create-CouncilPreamble','Result-CouncilPreamble','Download-documnets','Update Preamble','Search and Edit-CouncilPreamble','Search and View-CouncilPreamble','Search and View Result-CouncilPreamble','View-CouncilPreamble','Edit-CouncilPreamble','Search and Edit Result-CouncilPreamble');
insert into eg_roleaction values ((select id from eg_role where name='Council Management Creator'),(select id from eg_action where name='AjaxDesignationsByDepartment'));
insert into eg_roleaction values ((select id from eg_role where name='Council Management Creator'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));

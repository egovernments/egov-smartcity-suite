update eg_roleaction set roleid = (select id from eg_role where name='CSC Operator') where roleid = (select id from eg_role where name='Data Entry Operator');
UPDATE eg_role SET name='Property Verifier' WHERE name='PTIS Verifier';
UPDATE eg_role SET name='Property Approver' WHERE name='PTIS Approver';

INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action where name='LoginForm'),(SELECT id FROM eg_role where name='Property Verifier'));
INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action where name='LoginForm'),(SELECT id FROM eg_role where name='Property Approver'));

INSERT INTO egpt_status  (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'WORKFLOW',CURRENT_TIMESTAMP,'Y','WORKFLOW');
INSERT INTO egpt_status  (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'APPROVED',CURRENT_TIMESTAMP,'Y','APPROVED');

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Populate Designation','/common/ajaxCommon-populateDesignationsByDept.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Populate Designation',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Populate Approver','/common/ajaxCommon-populateUsersByDeptAndDesignation.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Populate Approver',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Populate Designation' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Populate Approver' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Populate Designation' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Populate Approver' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Inbox' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Inbox' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'InboxDraft' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'InboxDraft' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'InboxHistory' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'InboxHistory' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'Inbox' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'InboxDraft' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'InboxHistory' and CONTEXTROOT='egi'));

--DELETE FROM eg_roleaction WHERE actionid in (SELECT id FROM eg_action WHERE name in('InboxHistory','InboxDraft','Inbox','Populate Designation','Populate Approver','LoginForm')) and roleid in (SELECT id FROM eg_role WHERE name in('CSC Operator','Property Verifier','Property Approver'));
--DELETE FROM eg_action WHERE name in ('Populate Designation','Populate Approver') and contextroot='ptis';
--rollback UPDATE eg_role SET name='PTIS Verifier' WHERE name='Property Verifier';
--rollback UPDATE eg_role SET name='PTIS Approver' WHERE name='Property Approver';
--rollback DELETE FROM egpt_status WHERE status_name in('WORKFLOW','APPROVED');
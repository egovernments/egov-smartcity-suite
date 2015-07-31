
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'NEW', NULL, NULL, NULL, NULL, 'CREATED', 'ADD HEARING DATE', 'Commissioner',
 'CREATED', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'CREATED', NULL,'ADD HEARING DATE', NULL, NULL, 'Hearing date fixed', 
'Generate Hearing Notice', 'Assistant', 'HEARING DATE FIXED', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Hearing date fixed', NULL,'Generate Hearing Notice', NULL, NULL, 
'Record Hearing details', 'Record Hearing details', 'Revenue Inspector', 'GENERATE HEARING NOTICE', 'Forward,Print HearingNotice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Record Hearing details', NULL,'Record Hearing details', NULL, NULL, 
'Hearing completed', 'Record inspection details', 'Revenue Inspector', 'HEARING COMPLETED', 'Save', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Hearing completed', NULL,'Record inspection details', NULL, NULL, 
'Inspection completed', 'Verify inspection details', 'Revenue officer', 'INSPECTION COMPLETED', 'Save,Forward', NULL, NULL, '2015-04-01', '2099-04-01');


INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Inspection completed', NULL,'Verify inspection details', NULL, NULL, 
'Inspection verified', 'Final approval', 'Commissioner', 'INSPECTION VERIFY', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Inspection verified', NULL,'Final approval', NULL, NULL, 
'Approved', 'Print Endoresement', 'Assistant', null, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Approved', NULL,'Print Endoresement', NULL, NULL, 
'END', 'END', 'Assistant', null, 'Print Endoresement', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'Objection', 'Rejected', NULL,null, NULL, NULL, 
'Inspection completed', 'Verify inspection details', 'Revenue officer', null, 'Save,Forward', NULL, NULL, '2015-04-01', '2099-04-01');


INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'PropTax Rev Petition reject inspection',  now(),  now(), '/revPetition/revPetition-rejectInspectionDetails', 
null, (SELECT id FROM eg_module WHERE name='Ptis Revision Petition'), null, 'PropTax Rev Petition reject inspection', false,  'ptis',
(Select id from eg_module where name='Property Tax' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='PropTax Rev Petition reject inspection'),(Select id from eg_role where name='Property Verifier'));
insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='PropTax Rev Petition reject inspection'),(Select id from eg_role where name='Super User'));


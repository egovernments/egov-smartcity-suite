UPDATE eg_wf_matrix SET validactions = 'Forward,Cancel' WHERE currentstate = 'Rejected';

INSERT INTO eg_wf_matrix VALUES 
(nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Cancel', NULL, NULL, 'Operator', NULL, 'Operator Approved', 'Bill Collector Approved', 
'Bill Collector', 'Bill Collector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Populate Categories by Property Type' AND contextroot='ptis'),
(SELECT id FROM eg_role WHERE name='Property Verifier'));


--rollback UPDATE eg_wf_matrix SET validactions = 'Save,Cancel' WHERE currentstate = 'Rejected';
--rollback DELETE FROM eg_wf_matrix WHERE currentstate='Cancel';
--rollback DELETE FROM eg_roleaction WHERE actionid = (SELECT id FROM eg_action WHERE name='Populate Categories by Property Type' AND contextroot='ptis') AND roleid = (SELECT id FROM eg_role WHERE name='Property Verifier');

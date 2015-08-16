DELETE FROM eg_userrole WHERE roleid in(SELECT id FROM eg_role WHERE name='Redressal Officer') and userid in(SELECT id FROM eg_user WHERE username='surya');

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='CitizenInboxForm' and contextroot='egi'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Official Home Page' and contextroot='egi'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Modify Property Form' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Forward Modify Property' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Created', NULL, NULL, 'NULL','ALTER ASSESSMENT', 
'Alter' || ':' || 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 'Revenue Clerk Approved', 
'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation', 'Created', NULL, NULL, 'NULL','PROPERTY TRANSFER', 
'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 'Revenue Clerk Approved', 
'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Form' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Save' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));
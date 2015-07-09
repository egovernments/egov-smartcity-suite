INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Reject Property' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='Property Verifier'));
INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Reject Property' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='Property Approver'));

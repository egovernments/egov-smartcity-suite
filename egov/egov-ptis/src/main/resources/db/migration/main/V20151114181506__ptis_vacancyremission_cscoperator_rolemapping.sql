INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='vacancyRemissionCreate' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='vacancyRemissionUpdate' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));


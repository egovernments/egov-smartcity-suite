INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Search Property By Bndry' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Search Property By Location' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Search Property By Demand' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));

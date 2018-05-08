INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((select id from eg_role where name like 'CSC Operator'),(select id from eg_action where name='digitalSignature-TLDownloadSignDoc' and contextroot='tl'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((select id from eg_role where name like 'CSC Operator'),(select id from eg_action where name='Generate Provisional Certificate' and contextroot='tl'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'PUBLIC'), (SELECT id FROM eg_action WHERE name ='getWardsByLocality' and contextroot='egi'));


delete from eg_roleaction where roleid in (select id from eg_role where name in ('Property Administrator')) and actionid in (select id FROM eg_action WHERE NAME = 'PropTax Rev Petition Add hearing' and CONTEXTROOT='ptis');


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='PropTax Rev Petition Add hearing' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='Property Verifier'));

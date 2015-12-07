DELETE from eg_roleaction where roleid in (select id from eg_role where name in ('CSC Operator')) and actionid in (select id FROM eg_action WHERE NAME = 'CollectTax-Form' and CONTEXTROOT='ptis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='CollectTax-Form' and CONTEXTROOT='ptis'),(SELECT id FROM eg_role WHERE name ='ULB Operator'));


delete from eg_roleaction where actionid in (select id FROM eg_action WHERE NAME = 'Load Admin Boundaries' and url='/common/ajaxCommon-blockByLocality.action' and CONTEXTROOT='ptis');
delete from eg_roleaction where actionid in (select id FROM eg_action WHERE NAME = 'ajaxLoadBolockByWard' and url='/common/ajaxCommon-blockByWard.action' and CONTEXTROOT='ptis');
delete from eg_action where name = 'Load Admin Boundaries' and contextroot = 'ptis';
delete from eg_action where name = 'ajaxLoadBolockByWard' and contextroot = 'ptis';


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Locality' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Locality' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Locality' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='Property Verifier'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Locality' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Ward' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Ward' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='Property Verifier'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Load Block By Ward' and CONTEXTROOT='egi'),(SELECT id FROM eg_role WHERE name ='ULB Operator'));

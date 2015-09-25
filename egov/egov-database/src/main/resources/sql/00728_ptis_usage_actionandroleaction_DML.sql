INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
VALUES (nextval('seq_eg_action'), 'Property Usage by type', '/common/ajaxCommon-usageByPropType.action', null,(select id from eg_module where name='New Property'), 0, 'Property Usage by type', false, 'ptis', 0, 1, now(), 1, now(),
(select id from eg_module where name='Property Tax' and parentmodule is null)); 

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Property Usage by type' and contextroot='ptis'),(SELECT id FROM eg_role WHERE name ='ULB Operator'));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Property Usage by type' and contextroot='ptis'),(SELECT id FROM eg_role WHERE name ='CSC Operator'));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Property Usage by type' and contextroot='ptis'),(SELECT id FROM eg_role WHERE name ='Property Verifier'));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Property Usage by type' and contextroot='ptis'),(SELECT id FROM eg_role WHERE name ='Property Approver'));
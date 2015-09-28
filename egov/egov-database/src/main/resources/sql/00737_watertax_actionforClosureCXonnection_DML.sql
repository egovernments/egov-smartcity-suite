INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'CloseWaterConnectionApplication', '/application/close/', null,
     (select id from eg_module where name='Water Tax Management'), null, 'CloseWaterConnectionApplication', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));



INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='CloseWaterConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='CloseWaterConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'CloseWaterConnectionAcknowldgement', '/application/acknowlgementNotice', null,
     (select id from eg_module where name='Water Tax Management'), null, 'CloseWaterConnectionApplication', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='CloseWaterConnectionAcknowldgement' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='CloseWaterConnectionAcknowldgement' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));
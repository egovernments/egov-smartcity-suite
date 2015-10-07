
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'WaterReConnectionApplication', '/application/reconnection/', null,
     (select id from eg_module where name='Water Tax Management'), null, 'ReConnectionCreate', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));



INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='WaterReConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='WaterReConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'ReConnectionAcknowldgement', '/application/ReconnacknowlgementNotice', null,
     (select id from eg_module where name='Water Tax Management'), null, 'ReConnectionAcknowldgement', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='ReConnectionAcknowldgement' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='ReConnectionAcknowldgement' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'createReConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'createReConnection' and CONTEXTROOT='wtms'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'createReConnection','/search/waterSearch/commonSearch/', 'applicationType=RECONNECTION',
(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Create ReConnection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'createReConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'createReConnection' and CONTEXTROOT='wtms'));
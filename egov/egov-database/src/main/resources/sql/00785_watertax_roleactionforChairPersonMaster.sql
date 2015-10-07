INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'activechairpersonexistsasoncurrentdate-ajax', '/application/ajax-activeChairPersonExistsAsOnCurrentDate', null,(select id from eg_module where name='WaterTaxMasters'), null, 'addchairpersonname', false,'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='WaterTaxMasters' and  parentmodule in(Select id from eg_module where name='Water Tax Management')));
    
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator') ,(select id FROM eg_action  WHERE name = 'activechairpersonexistsasoncurrentdate-ajax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'activechairpersonexistsasoncurrentdate-ajax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'addchairpersonname-ajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'ChairPersonDetailsScreen'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'getchairpersontableajax'));

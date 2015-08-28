INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'ChairPersonDetailsScreen', '/application/chairPersonDetails.action', null, (select id from eg_module where name='WaterTaxMasters'), 1, 'Create Chair Person', true, 'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='WaterTaxMasters' and parentmodule in(Select id from eg_module where name='Water Tax Management')));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'getchairpersonname-ajax', '/application/ajax-chairPersonName', null,(select id from eg_module where name='WaterTaxMasters'), null, 'getchairpersonname', false,'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='WaterTaxMasters' and  parentmodule in(Select id from eg_module where name='Water Tax Management')));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'getchairpersontableajax', '/application/ajax-chairpersontable', null,(select id from eg_module where name='WaterTaxMasters'), null, 'getchairpersontable', false,'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='WaterTaxMasters' and parentmodule in(Select id from eg_module where name='Water Tax Management')));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator') ,(select id FROM eg_action  WHERE name = 'ChairPersonDetailsScreen'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator') ,(select id FROM eg_action  WHERE name = 'getchairpersonname-ajax'));
    
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator') ,(select id FROM eg_action  WHERE name = 'getchairpersontableajax'));
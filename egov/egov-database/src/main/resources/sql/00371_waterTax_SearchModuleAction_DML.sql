INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'watertaxappsearch', '/search/waterSearch/', null, 
    (select id from eg_module where name='WaterTaxApplication'), null, 
    'Search WaterTax Application', 't', 'wtms', 0, 1, now(), 1, now());


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,
(select id FROM eg_action  WHERE name = 'watertaxappsearch'));
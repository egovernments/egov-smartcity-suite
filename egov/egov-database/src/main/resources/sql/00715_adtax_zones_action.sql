INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'ZoneAjaxDropdown', '/hoarding/child-boundaries', null, 
    (select id from eg_module where name='ADTAX-COMMON'), 1, 'ZoneAjaxDropdown', false, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'ZoneAjaxDropdown'));


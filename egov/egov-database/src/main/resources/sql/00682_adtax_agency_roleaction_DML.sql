update eg_module set enabled  =true where parentmodule=(select id from eg_module where name='Advertisement Tax');

--create agency

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'CreateAgency', '/agency/create', null, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Create Agency', true, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='CreateAgency' and contextroot='adtax';
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAgency'));

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='CreateAgency' and contextroot='adtax');

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AgencySuccess', '/agency/success', null, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Agency Success', false, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='AgencySuccess' and contextroot='adtax';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencySuccess'));

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='AgencySuccess' and contextroot='adtax');

---search/update agency

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'SearchAgency', '/agency/search', null, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Update Agency', true, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='SearchAgency' and contextroot='adtax';
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SearchAgency'));

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='SearchAgency' and contextroot='adtax');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AgencyUpdate', '/agency/update', null, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Agency Update', false, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='AgencySuccess' and contextroot='adtax';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencyUpdate'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='AgencyUpdate' and contextroot='adtax');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'CreateHoarding', '/hoarding/create', null, 
    (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Create Hoarding', true, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'CreateHoarding'));

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'ADTAX-COMMON', false, null, (select id from eg_module where name='Advertisement Tax'), 'ADTAX-COMMON', 1);


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AgencyAjaxDropdown', '/agency/agencies', null, 
    (select id from eg_module where name='ADTAX-COMMON'), 1, 'AgencyAjaxDropdown', false, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'AgencyAjaxDropdown'));

ALTER TABLE egadtax_hoarding DROP CONSTRAINT fk_adtax_hoarding_propertytype;

DROP TABLE egadtax_propertytype;

ALTER TABLE egadtax_hoarding RENAME advertisement_particular TO advertisementParticular;

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'SubcategoryAjaxDropdown', '/hoarding/subcategories', null, 
    (select id from eg_module where name='ADTAX-COMMON'), 1, 'SubcategoryAjaxDropdown', false, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'SubcategoryAjaxDropdown'));


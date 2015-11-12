------------------START------------------
alter table EGADTAX_HOARDING add column electricityServiceNumber character varying(25);
alter table egadtax_hoarding alter column hoardingname DROP not null; 
update egadtax_subcategory set version=0 where version is null;

------------------END------------------
------------------START------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CreateSubcategory', '/subcategory/create', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 10, 'Create sub category', true, 'adtax', 0, 1, current_date, 1, current_date, (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'subcategorySuccess', '/subcategory/success', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 11, 'Sub category  Success', false, 'adtax', 0, 1, current_date, 1, current_date, (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'subcategoryUpdate', '/subcategory/update', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 12, 'Sub category Update', false, 'adtax', 0, 1, current_date, 1, current_date, (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Searchsubcategory', '/subcategory/search', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 13, 'Search sub category', true, 'adtax', 0, 1, current_date, 1, current_date, (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateSubcategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'subcategorySuccess'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'subcategoryUpdate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'Searchsubcategory'));
------------------END------------------
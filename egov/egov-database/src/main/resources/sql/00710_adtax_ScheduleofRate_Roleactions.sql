
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'scheduleOfRateOnLoad', '/rates/search', null,
     (select id from eg_module where name='AdvertisementTaxMasters'), null, 'Create Schedule Of Rate', true,
     'adtax', 0, 1, now(), 1, now(),(Select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AjaxSubCategoryByCategoryId', '/ajax-subCategories', null,
     (select id from eg_module where name='AdvertisementTaxMasters'), null, 'Ajax Call for subcategory', false,
     'adtax', 0, 1, now(), 1, now(),(Select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'createScheduleOfRate', '/rates/create', null,
     (select id from eg_module where name='AdvertisementTaxMasters'), null, 'Save Schedule Of Rate', false,
     'adtax', 0, 1, now(), 1, now(),(Select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'savedScheduleOfRate', '/rates/success', null,
     (select id from eg_module where name='AdvertisementTaxMasters'), null, 'Saved Schedule Of Rate', false,
     'adtax', 0, 1, now(), 1, now(),(Select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  
WHERE NAME = 'AjaxSubCategoryByCategoryId' and CONTEXTROOT='adtax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  
WHERE NAME = 'scheduleOfRateOnLoad' and CONTEXTROOT='adtax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  
WHERE NAME = 'createScheduleOfRate' and CONTEXTROOT='adtax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  
WHERE NAME = 'savedScheduleOfRate' and CONTEXTROOT='adtax'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'SearchHoarding', '/hoarding/search', null, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Search Hoarding', true, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='SearchHoarding' and contextroot='adtax';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SearchHoarding'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='SearchHoarding' and contextroot='adtax');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AjaxSubCategories', '/hoarding/subcategories-by-category', null, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Ajax load subcategories', false, 'adtax', 0, 1, now(), 1, now(),(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

--rollback delete from eg_action where name='AjaxSubCategories' and contextroot='adtax';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxSubCategories'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='AjaxSubCategories' and contextroot='adtax');




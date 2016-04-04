
insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'SubCategory search', '/reports/subcategory/searchSubCategory',  NULL, (select id from eg_module where name='Advertisement Tax Subcategory'), 1,   'SubCategory search', false, 'adtax', 0, 1, current_date, 1, current_date,    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
    
insert into EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SubCategory search'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='SubCategory search'));




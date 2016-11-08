
-- Create Role ADTAX_VIEW_ACCESS_ROLE and role-action mappings

INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'ADTAX_VIEW_ACCESS_ROLE', 'user has access to view masters, reports, transactional data, etc', now(), 1, 1, now(), 0);

--ADTAX Masters view role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='ADTAX_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Searchcategory','categorySuccess','Searchsubcategory','subcategorySuccess','SubCategory search','SearchUnitOfMeasure','UnitOfMeasureSuccess','savedScheduleOfRate','Search ScheduleOfRate','Search ScheduleOfRate','AjaxSubCategoryByCategoryId','SearchAgency','AgencySuccess','AgencyView','Search TPBO','Tpbo Update','Search Saved Revenue Inspectors','SearchRateClass','savedRateClass') and contextroot = 'adtax' );


--Transactions view role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='ADTAX_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('searchadvertisement','HoardingView','AjaxSubCategories','Load Block By Locality','searchadvertisementResult') and contextroot = 'adtax' );


--Reports role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='ADTAX_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('HoardingDcbReport','dcbReportSearch','Agency Wise DCB Report','Search Agency Hoardings','Agencywise Collection Report') and contextroot = 'adtax' );
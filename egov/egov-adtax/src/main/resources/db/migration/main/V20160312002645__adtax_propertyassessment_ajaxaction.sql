
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'AjaxGetPropertyassessmentDetails', '/ajax-assessmentDetails', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), NULL, 'Ajax Call to get propertydetail', false, 'adtax', 0, 1, '2015-09-23 15:22:18.126628', 1, '2015-09-23 15:22:18.126628', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,
(select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'Advertisement Tax Creator') ,
(select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails'));
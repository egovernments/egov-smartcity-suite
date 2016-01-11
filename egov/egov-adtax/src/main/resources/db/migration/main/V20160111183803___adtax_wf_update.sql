alter table egadtax_category add propertymandatory boolean NOT NULL DEFAULT false;

UPDATE EG_Wf_TYPES SET LINK='/adtax/hoarding/update/:ID' where type='AdvertisementPermitDetail';
update eg_wf_matrix set objecttype='AdvertisementPermitDetail' where objectType='Advertisement';
update eg_wf_matrix set currentdesignation='Commissioner' where objecttype='AdvertisementPermitDetail' and currentstate='Asst. approved';
update eg_wf_matrix set nextdesignation='Commissioner' where objecttype='AdvertisementPermitDetail' and nextstate='Asst. approved';
update egw_status set description='Created' where code='CREATED';
update eg_wf_matrix set nextstatus='CREATED',currentstate='New' where objecttype='AdvertisementPermitDetail' and currentstate='NEW';
update eg_wf_matrix set nextstatus='APPROVED' where objecttype='AdvertisementPermitDetail' and currentstate='Rejected';

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Advertisement Tax Creator', 'Advertisement Tax Creator', now(), 1, 1, now(), 0);
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Advertisement Tax Approver', 'Advertisement Tax Approver', now(), 1, 1, now(), 0);


insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='CreateHoarding'));
insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='AgencyAjaxDropdown'));
insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='SubcategoryAjaxDropdown'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='Load Block By Locality'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='SubcategoryAjaxDropdown'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='calculateTaxAmount'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='AjaxDesignationsByDepartment'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'HoardingSuccess', '/hoarding/success', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'HoardingSuccess', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingSuccess'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'HoardingSuccess'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='HoardingUpdate'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='SubcategoryAjaxDropdown'));

insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='HoardingView'));

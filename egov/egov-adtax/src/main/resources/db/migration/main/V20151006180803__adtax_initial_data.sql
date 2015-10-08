
-----------------START--------------------
INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Advertisement Tax', true, 'wtms', null, 'Advertisement Tax', (select max(ordernumber)+1 from eg_module where parentmodule is null));

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxMasters', true, null, (select id from eg_module where name='Advertisement Tax'), 'Masters', 1);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxTransactions', true, null, (select id from eg_module where name='Advertisement Tax'), 'Transactions', 2);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxReports', false, null, (select id from eg_module where name='Advertisement Tax'), 'Reports', 3);


INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'ADTAX-COMMON', false, null, (select id from eg_module where name='Advertisement Tax'), 'ADTAX-COMMON', 1);

------------------END---------------------
-----------------START--------------------



Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null),current_timestamp,'ADTAX/15-16','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042016,to_date('01-04-16','DD-MM-YY'),to_date('01-04-16','DD-MM-YY'),to_date('31-03-17','DD-MM-YY'),(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null),current_timestamp,'ADTAX/16-17','Yearly');
------------------END---------------------
-----------------START--------------------
--demand reason master
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Enchroachment Fee', (select id from eg_reason_category where code='FINES'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Enchroachmnt_Fee', 1, current_timestamp, current_timestamp,'t');

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Advertisement Tax', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Advertisemnt_Tax', 2, current_timestamp, current_timestamp,'t');

------------------END---------------------
-----------------START--------------------
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Advertisement Tax' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Enchroachment Fee' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));

INSERT INTO EG_DEMAND_REASON_MASTER(ID, REASONMASTER, "category", ISDEBIT, MODULE, CODE, "order", CREATE_DATE, MODIFIED_DATE,isdemand) 
VALUES(nextval('SEQ_EG_DEMAND_REASON_MASTER'), 'Penalty',(select ID from EG_REASON_CATEGORY where NAME='FINES'), 'N', (select id from eg_module where NAME='Advertisement Tax'), 'Penalty', 3, now(),  now(),'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Penalty' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1402002') from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));
------------------END---------------------
-----------------START--------------------
--Configuration to calculate penalty
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 
'Penalty Calculation required', 
'Penalty Calculation required',0, (select id from eg_module where name='Advertisement Tax')); 
------------------END---------------------
-----------------START--------------------
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Penalty Calculation required' AND 
 MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,
  'YES',0);

------------------END---------------------
-----------------START--------------------

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CreateAgency', '/agency/create', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Create Agency', true, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AgencySuccess', '/agency/success', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Agency Success', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AgencyUpdate', '/agency/update', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Agency Update', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'SearchAgency', '/agency/search', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Search Agency', true, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AgencyView', '/agency/view', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), 1, 'Agency View', false, 'adtax', 0, 1, '2015-09-18 11:15:24.520062', 1, '2015-09-18 11:15:24.520062', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CreateHoarding', '/hoarding/create', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Create Hoarding', true, 'adtax', 0, 1, '2015-09-22 17:08:52.811193', 1, '2015-09-22 17:08:52.811193', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AgencyAjaxDropdown', '/agency/agencies', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'AgencyAjaxDropdown', false, 'adtax', 0, 1, '2015-09-22 17:08:52.811193', 1, '2015-09-22 17:08:52.811193', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'SubcategoryAjaxDropdown', '/hoarding/subcategories', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'SubcategoryAjaxDropdown', false, 'adtax', 0, 1, '2015-09-22 17:08:52.811193', 1, '2015-09-22 17:08:52.811193', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'scheduleOfRateOnLoad', '/rates/search', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), NULL, 'Create Schedule Of Rate', true, 'adtax', 0, 1, '2015-09-23 15:22:18.126628', 1, '2015-09-23 15:22:18.126628', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AjaxSubCategoryByCategoryId', '/ajax-subCategories', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), NULL, 'Ajax Call for subcategory', false, 'adtax', 0, 1, '2015-09-23 15:22:18.126628', 1, '2015-09-23 15:22:18.126628', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'createScheduleOfRate', '/rates/create', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), NULL, 'Save Schedule Of Rate', false, 'adtax', 0, 1, '2015-09-23 15:22:18.126628', 1, '2015-09-23 15:22:18.126628', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'savedScheduleOfRate', '/rates/success', NULL, (select id from eg_module where name='AdvertisementTaxMasters'), NULL, 'Saved Schedule Of Rate', false, 'adtax', 0, 1, '2015-09-23 15:22:18.126628', 1, '2015-09-23 15:22:18.126628', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AjaxSubCategories', '/hoarding/subcategories-by-category', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'Ajax load subcategories', false, 'adtax', 0, 1, '2015-09-23 15:22:18.385446', 1, '2015-09-23 15:22:18.385446', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CollectAdvertisementTax', '/hoarding/search', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 2, 'Collect Advertisement Tax', true, 'adtax', 0, 1, '2015-09-23 15:22:18.385446', 1, '2015-09-23 15:22:18.385446', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'ZoneAjaxDropdown', '/hoarding/child-boundaries', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'ZoneAjaxDropdown', false, 'adtax', 0, 1, '2015-09-24 18:52:49.453294', 1, '2015-09-24 18:52:49.453294', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'searchHoardingResult', '/hoarding/search-list', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'search HoardingResult', false, 'adtax', 0, 1, '2015-09-24 18:52:49.736388', 1, '2015-09-24 18:52:49.736388', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'calculateTaxAmount', '/hoarding/calculateTaxAmount', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'ZoneAjaxDropdown', false, 'adtax', 0, 1, '2015-09-27 12:31:31.510653', 1, '2015-09-27 12:31:31.510653', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'HoardingSearchUpdate', '/hoarding/search-for-update', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 2, 'Update Hoarding', true, 'adtax', 0, 1, '2015-10-01 16:45:31.200641', 1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'HoardingUpdate', '/hoarding/update', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Hoarding Update', false, 'adtax', 0, 1, '2015-10-01 16:45:31.200641', 1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'HoardingView', '/hoarding/view', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Hoarding View', false, 'adtax', 0, 1, '2015-10-01 16:45:31.200641', 1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'generateBillForCollection', '/hoarding/generatebill', NULL, (select id from eg_module where name='ADTAX-COMMON'), 1, 'generateBillForCollection', false, 'adtax', 0, 1, '2015-10-01 07:07:06.340474', 1, '2015-10-01 07:07:06.340474', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CreateLegacyHoarding', '/hoarding/createLegacy', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Create Legacy Hoarding', true, 'adtax', 0, 1, '2015-10-06 15:17:21.578611', 1, '2015-10-06 15:17:21.578611', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

------------------END---------------------
-----------------START--------------------


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAgency'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencySuccess'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencyUpdate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SearchAgency'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencyView'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateHoarding'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AgencyAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SubcategoryAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'scheduleOfRateOnLoad'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxSubCategoryByCategoryId'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'createScheduleOfRate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'savedScheduleOfRate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxSubCategories'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CollectAdvertisementTax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ZoneAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingSearchUpdate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingUpdate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingView'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'generateBillForCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateLegacyHoarding'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'calculateTaxAmount'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'searchHoardingResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));

------------------END---------------------
-----------------START--------------------
update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101')  where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Advertisement Tax' and module=(select id from eg_module where name='Advertisement Tax'));

update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101') where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Enchroachment Fee' and module=(select id from eg_module where name='Advertisement Tax'));

INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'Advertisement Tax', 'ADTAX', true, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'Advertisement Tax','/../adtax/hoarding/generatebill',true,'/receipts/receipt-create.action',
'B','ADTAX',null,null,null,true,null,null,(select id from egcl_servicecategory where code='ADTAX'),true,to_date('11-07-15','DD-MM-RR'),1,current_timestamp,1,current_timestamp);


------------------END---------------------
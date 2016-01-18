
----start

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Arrears Tax', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Arrear_Adv_Tax', 3, current_timestamp, current_timestamp,'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Arrears Tax' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));

update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101')  where id_demand_reason_master in ( select id from eg_demand_reason_master where reasonmaster='Arrears Tax' and module=(select id from eg_module where name='Advertisement Tax'));

--end

update eg_action set displayname='Update Legacy Advertisements' where name='HoardingSearchUpdate';

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'HoardingLegacyUpdate', '/hoarding/updateLegacy', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Legacy Hoarding Update', false, 'adtax', 0, 1, '2015-10-01 16:45:31.200641', 1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingLegacyUpdate'));

--------
update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101')  where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Advertisement Tax' and module=(select id from eg_module where name='Advertisement Tax'));

update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101') where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Enchroachment Fee' and module=(select id from eg_module where name='Advertisement Tax'));

-- Service category details 
select  nextval('seq_egcl_servicecategory');

INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'Advertisement Tax', 'ADTAX', true, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'Advertisement Tax','/../adtax/hoarding/generatebill',true,'/receipts/receipt-create.action',
'B','ADTAX',1,null,null,true,null,null,(select id from egcl_servicecategory where code='ADTAX'),true,to_date('11-07-15','DD-MM-RR'),1,current_timestamp,1,current_timestamp);



insert INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
  VALUES (nextval('seq_eg_action'), 'generateBillForCollection', '/hoarding/generatebill', null, (select id from eg_module where name='ADTAX-COMMON'), 1, 'generateBillForCollection', false, 'adtax', 0, 1, now(), 1, now(),    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'generateBillForCollection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CSC Operator') , (select id FROM eg_action  WHERE name = 'generateBillForCollection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));


-- Demand reasons for penalty

INSERT INTO EG_DEMAND_REASON_MASTER(ID, REASONMASTER, "category", ISDEBIT, MODULE, CODE, "order", CREATE_DATE, MODIFIED_DATE,isdemand) 
VALUES(nextval('SEQ_EG_DEMAND_REASON_MASTER'), 'Penalty',(select ID from EG_REASON_CATEGORY where NAME='FINES'), 'N', (select id from eg_module where NAME='Advertisement Tax'), 'Penalty', 3, now(),  now(),'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Penalty' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1402002') from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));

--Configuration to calculate penalty
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 
'Penalty Calculation required', 
'Penalty Calculation required',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Penalty Calculation required' AND 
 MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,
  'YES',0);


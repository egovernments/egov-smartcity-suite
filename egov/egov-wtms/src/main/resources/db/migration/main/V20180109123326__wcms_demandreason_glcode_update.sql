

INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date,isdemand) VALUES (nextval('seq_eg_demand_reason_master'), 'Water Tax Meter charges', (select id from eg_reason_category where code='TAX'),'N',(select id from eg_module where name = 'Water Tax Management'),'METERCHARGES', 2, now(), now(),true);

update eg_demand_reason set id_demand_reason_master = (select id from eg_demand_reason_master where code='METERCHARGES' and module = (select id from eg_module where name='Water Tax Management')) where id_installment in (select id from eg_installment_master where installment_type ='Monthly' and description like 'WT_MC%');

update eg_demand_reason  set glcodeid = (select id from chartofaccounts where glcode = '1405016') where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'METERCHARGES' and module=(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 now(),  'METERCHARGES=1405016',0,now(),now(),(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'));


update eg_appconfig_values set value='WTAXCHARGES=1806006' where value='WTAXCHARGES=1405016' and key_id in (select id from eg_appconfig where key_name ='DemandReasonGlcodeMap' and module = (select id from eg_module where name='Water Tax Management'));

update eg_demand_reason  set glcodeid = (select id from chartofaccounts where glcode = '1806006') where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'WTAXCHARGES' and module=(select id from eg_module where name='Water Tax Management'));

update eg_demand_reason  set glcodeid = (select id from chartofaccounts where glcode = '1603005') where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'WTAXDONATION' and module=(select id from eg_module where name='Water Tax Management'));



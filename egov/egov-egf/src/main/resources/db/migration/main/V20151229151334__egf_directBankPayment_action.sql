Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Direct Bank Payments-Create','/payment/directBankPayment-create.action',null,(select id from eg_module where name='Payments'),1,'Edit Search SubScheme',false,'EGF',0,1,to_timestamp('2015-08-15 11:04:27.32357','null'),1,to_timestamp('2015-08-15 11:04:27.32357','null'),(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Direct Bank Payments-Create'));



update eg_appconfig_values set value = 'N' where key_id in (select id from eg_appconfig where key_name ='Balance Check Based on Fund Flow Report');

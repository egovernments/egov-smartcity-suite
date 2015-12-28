Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ModifySubScheme','/masters/subScheme-edit.action',null,(select id from eg_module where name='Schemes'),1,'Edit Search SubScheme',false,'EGF',0,1,to_timestamp('2015-08-15 11:04:27.32357','null'),1,to_timestamp('2015-08-15 11:04:27.32357','null'),(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ModifySubScheme'));



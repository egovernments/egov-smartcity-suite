Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ServiceToBankMapping','/service/serviceTypeToBankAccountMapping-newform.action',null,(select id from eg_module where name='Collection Master'),1,' Service To Bank Mapping',true,'collection',0,1,to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceToBankMapping'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'ServiceTypeToBankAccountMappingCreate','/service/serviceTypeToBankAccountMapping-create.action',null,
(select id from eg_module where name='Collection Master'),1,'Create Service To Bank Mapping',false,'collection',0,1,
to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceTypeToBankAccountMappingCreate'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'ServiceTypeToBankAccountMappingSave','/service/serviceTypeToBankAccountMapping-save.action',null,
(select id from eg_module where name='Collection Master'),1,'Save Service To Bank Mapping',false,'collection',0,1,
to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceTypeToBankAccountMappingSave'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'ServiceTypeToBankAccountMappingList','/service/serviceTypeToBankAccountMapping-list.action',null,
(select id from eg_module where name='Collection Master'),1,'List Service To Bank Mapping',false,'collection',0,1,
to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceTypeToBankAccountMappingList'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'ServiceTypeToBankAccountMappingEdit','/service/serviceTypeToBankAccountMapping-edit.action',null,
(select id from eg_module where name='Collection Master'),1,'List Service To Bank Mapping',false,'collection',0,1,
to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceTypeToBankAccountMappingEdit'));



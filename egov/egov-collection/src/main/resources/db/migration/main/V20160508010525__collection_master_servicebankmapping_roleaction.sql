update eg_action set enabled =true, displayname='View/Modify Bank Service Mapping' where name='ServiceTypeToBankAccountMappingList';

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'ServiceTypeToBankAccountMappingSearch','/service/serviceTypeToBankAccountMapping-search.action',null,(select id from eg_module where name='Collection Master'),1,'ServiceTypeToBankAccountMappingSearch',false,'collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ServiceTypeToBankAccountMappingSearch'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, 
enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'LinkActiveProperty','/application/linkedAssessment',null,(select id from eg_module where name='WaterTaxTransactions'),1,'Link Active Property',true,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='LinkActiveProperty'));



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'ajax-getpropertyidbyConnection','/ajax-getPropertyIdByConsumerCode',null,(select id from eg_module
 where name='WaterTaxTransactions'),6,'ajax-getpropertyidbyConnection','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='ajax-getpropertyidbyConnection'));
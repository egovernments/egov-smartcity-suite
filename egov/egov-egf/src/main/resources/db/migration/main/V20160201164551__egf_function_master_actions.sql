Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'FunctionSearch','/masters/function-search.action',null,(select id from eg_module where name='EGF-COMMON'),1,'FunctionSearch',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='FunctionSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='FunctionSearch'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'FunctionEdit','/masters/function-edit.action',null,(select id from eg_module where name='EGF-COMMON'),1,'FunctionEdit',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='FunctionEdit'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='FunctionEdit'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'FunctionCreate','/masters/function-create.action',null,(select id from eg_module where name='EGF-COMMON'),1,'FunctionCreate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='FunctionCreate'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='FunctionCreate'));


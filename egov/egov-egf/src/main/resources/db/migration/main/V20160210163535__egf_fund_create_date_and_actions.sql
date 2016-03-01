update fund set created = current_date , purpose_id = null ;

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'FundSearch','/masters/fund-search.action',null,(select id from eg_module where name='Fund'),1,'FundSearch',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='FundSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='FundSearch'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'FundEdit','/masters/fund-edit.action',null,(select id from eg_module where name='Fund'),1,'FundEdit',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='FundEdit'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='FundEdit'));



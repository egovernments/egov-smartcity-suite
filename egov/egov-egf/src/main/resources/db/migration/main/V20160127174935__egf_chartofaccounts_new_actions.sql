Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChartOfAccountsAddNewCoa','/masters/chartOfAccounts-addNewCoa.action',null,(select id from eg_module where name='EGF-COMMON'),1,'ChartOfAccountsAddNewCoa',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsAddNewCoa'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccountsAddNewCoa'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChartOfAccountsSave','/masters/chartOfAccounts-save.action',null,(select id from eg_module where name='EGF-COMMON'),1,'ChartOfAccountsSave',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsSave'));

Insert into eg_roleaction   values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccountsSave'));

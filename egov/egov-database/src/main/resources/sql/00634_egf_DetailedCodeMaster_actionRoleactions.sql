Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ChartOfAccountsSearchView','/masters/chartOfAccounts-viewSearch.action',
(select id from eg_module where name='Chart of Accounts'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsSearchView'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ChartOfAccountsSearchModify','/masters/chartOfAccounts-modifySearch.action',
(select id from eg_module where name='Chart of Accounts'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsSearchModify'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ChartOfAccountsUpdate','/masters/chartOfAccounts-update.action',
(select id from eg_module where name='Chart of Accounts'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsUpdate'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ChartOfAccountsModify','/masters/chartOfAccounts-modify.action',
(select id from eg_module where name='Chart of Accounts'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsModify'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ChartOfAccountsCreate','/masters/chartOfAccounts-create.action',
(select id from eg_module where name='Chart of Accounts'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsCreate'));

--------------------------- START ------------------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Get-Parent-Accounts','/assetcategory/getParentAccounts',
(select id from eg_module where name='Asset Masters Category' ),1,'Get-Parent-Accounts',false,'egassets',
(select id from eg_module where name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Get-Parent-Accounts'));
--------------------------- END --------------------------------

--Rollback DELETE FROM eg_roleaction where actionid = (select id from eg_action where name='Get-Parent-Accounts');
--Rollback DELETE FROM eg_action WHERE name='Get-Parent-Accounts';

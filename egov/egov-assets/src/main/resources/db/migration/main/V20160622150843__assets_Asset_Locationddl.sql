------------------------------------------------- START -------------------------------------------------------------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Get-Boundaries','/asset/getBoundariesByLocation',
(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Get-Boundaries',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Get-Boundaries'));

------------------------------------------------- END   -------------------------------------------------------------------------

--Rollback DELETE FROM eg_roleaction where actionid = (select id from eg_action where name= 'Get-Boundaries');
--Rollback DELETE FROM eg_action WHERE name='Get-Boundaries';

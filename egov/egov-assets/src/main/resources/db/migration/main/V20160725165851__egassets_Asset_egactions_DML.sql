------------------------------------------------- START -------------------------------------------------------------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Asset-Show-Search-Page','/asset/showsearchpage',(select id from eg_module where name='Asset Transactions' and 
parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Asset-Show-Search-Page',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Asset-Show-Search-Page'));
------------------------------------------------- END ---------------------------------------------------------------------------
--Rollback DELETE FROM eg_roleaction where actionid = (select id from eg_action where name= 'Asset-Show-Search-Page');
--Rollback DELETE FROM eg_action WHERE name='Asset-Show-Search-Page';

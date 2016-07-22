---------------------------------------------- START -----------------------------------------------------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Get-Master-Records','/assetcategory/masterdata',
(select id from eg_module where name='Asset Masters Category' ),1,'Get-Master-Records',false,'egassets',
(select id from eg_module where name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Get-Master-Records'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Delete-Category-Property','/assetcategory/categoryproperty/delete',
(select id from eg_module where name='Asset Masters Category' ),1,'Delete-Category-Property',false,'egassets',
(select id from eg_module where name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Delete-Category-Property'));
---------------------------------------------- END -------------------------------------------------------------------



--Rollback DELETE FROM eg_roleaction where actionid = (select id from eg_action where name='Get-Master-Records');
--Rollback DELETE FROM eg_action WHERE name='Get-Master-Records';

--Rollback DELETE FROM eg_roleaction where actionid = (select id from eg_action where name='Delete-Category-Property');
--Rollback DELETE FROM eg_action WHERE name='Delete-Category-Property';

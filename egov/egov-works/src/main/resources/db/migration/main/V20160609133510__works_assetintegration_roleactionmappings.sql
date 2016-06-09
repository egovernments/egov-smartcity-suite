------------Role action mappings to search Asset-----------
Insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'WorksSearchAsset'), id from eg_role where name in ('Works Creator');
Insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'WorksSearchAssetSearchResult'), id from eg_role where name in ('Works Creator');
Insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ShowAssetDetails'), id from eg_role where name in ('Works Creator');

---Rollback delete from eg_roleaction where actionid in (select id from eg_action where name in('WorksSearchAsset','WorksSearchAssetSearchResult','ShowAssetDetails')) and roleid = (select id from eg_role where name='Works Creator');


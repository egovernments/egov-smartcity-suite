----Roleactions for asset integration--
insert into eg_roleaction  (roleid,actionid) values ((select id from eg_role where name='Works Creator'),(select id from eg_action where name='Asset-Show-Search-Page' and contextroot='egassets'));
insert into eg_roleaction  (roleid,actionid) values ((select id from eg_role where name='Works Creator'),(select id from eg_action where name='Search and View Result-Asset' and contextroot='egassets'));
insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='View-Asset'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='View-Asset'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='Asset-Show-Search-Page'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='Search and View Result-Asset'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in('Asset-Show-Search-Page','Search and View Result-Asset','View-Asset')) and roleid in(select id from eg_role where name in('Works Creator','Works Approver'));
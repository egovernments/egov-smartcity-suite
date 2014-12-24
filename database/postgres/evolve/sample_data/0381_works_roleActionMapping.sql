#UP

insert into eg_roleaction_map (actionid,roleid) 
values((select ID from eg_action where name='searchTenderfile'),(select id_role from eg_roles where role_name='SuperUser'));

insert into eg_roleaction_map (actionid,roleid) 
values((select ID from eg_action where name='searchTenderfile'),(select id_role from eg_roles where role_name='Works Creator'));




#DOWN

Delete from EG_ROLEACTION_MAP 
where roleid in (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME='SuperUser') 
and  actionid in (SELECT ID FROM EG_ACTION WHERE NAME='searchTenderfile');

Delete from EG_ROLEACTION_MAP 
where roleid in (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME='Works Creator') 
and  actionid in (SELECT ID FROM EG_ACTION WHERE NAME='searchTenderfile');

#UP
insert into eg_roleaction_map values((select id_role from eg_roles where role_name='Estimate Creator'),(select id from eg_action where name='CreateWorksPackage'));
#DOWN
delete from eg_roleaction_map where actionid in (select id from eg_action where name='CreateWorksPackage');

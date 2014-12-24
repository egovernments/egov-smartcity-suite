#UP

insert into eg_roleaction_map (actionid, roleid) values
 ((select id from eg_action where name='Cheque Assignment For Remittances'),(select id_role from eg_roles where role_name='SuperUser'));

#DOWN
delete eg_roleaction_map where actionid=(select id from eg_action where name='Cheque Assignment For Remittances') and roleid=(select id_role from eg_roles where role_name='SuperUser');


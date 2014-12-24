#UP

insert into eg_roleaction_map values((select id_role from eg_roles where role_name='Works Creator'),
(select id from eg_action where name='searchEstimateTemplate'));

#DOWN


delete from eg_roleaction_map where roleid=(select id_role from eg_roles where role_name='Works Creator') and actionid =(select id from eg_action where name ='searchEstimateTemplate');

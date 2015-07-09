delete from eg_roleaction where actionid = (select id from eg_action where name = 'Modify Property Action');

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Modify Property Action'));

--delete from eg_roleaction where roleid in (select id from eg_role where name='CSC Operator') and actionid in (select id from eg_action where name='Modify Property Action');
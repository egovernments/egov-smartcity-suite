insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='Forward Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Reject Modify Property'));

--delete from eg_roleaction where roleid in (select id from eg_role where name='CSC Operator') and actionid in (select id from eg_action where name='Reject Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='Property Verifier') and actionid in (select id from eg_action where name='Forward Modify Property');
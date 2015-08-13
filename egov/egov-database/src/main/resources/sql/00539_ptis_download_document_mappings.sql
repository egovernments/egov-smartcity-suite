insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Property Verifier'), (select id from eg_action  where name = 'File Download'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Property Approver'), (select id from eg_action  where name = 'File Download'));

--rollback delete from eg_roleaction  where roleid in (select id from eg_role where name in ('Property Verifier', 'Property Approver')) and actionid in ((select id from eg_action  where name = 'File Download'));
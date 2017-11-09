Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='ajaxForExistingConsumerCodeFordataEntry'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('ajaxForExistingConsumerCodeFordataEntry') and contextroot = 'wtms') and roleid in(select id from eg_role where name = 'Super User'); 
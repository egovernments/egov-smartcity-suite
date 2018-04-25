
delete from eg_roleaction where actionid in (select id from eg_action where name in ('LinkActiveProperty') and contextroot = 'wtms') and roleid in(select id from eg_role where name = 'Property Administrator');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='LinkActiveProperty'));




delete from eg_roleaction where actionid in(select id from eg_action where name='Unit Rate Master' and contextroot='ptis');

insert into eg_roleaction  (actionid,roleid) values((select id from eg_action where name='Unit Rate Master' and contextroot='ptis'),
(select id from eg_role where name='Property Administrator'));

insert into eg_roleaction  (actionid,roleid) values((select id from eg_action where name='Unit Rate Create' and contextroot='ptis'),
(select id from eg_role where name='Property Administrator'));
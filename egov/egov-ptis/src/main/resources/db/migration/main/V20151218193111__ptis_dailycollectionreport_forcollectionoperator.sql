delete from eg_roleaction where actionid in (select id from eg_action where name in ('Daily collection report', 'Daily collection report result') and url in ('/report/dailyCollection', '/report/dailyCollection/result')) and roleid=(select id from eg_role where name='Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report'), id from eg_role where name in ('Super User','Collection Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report result'), id from eg_role where name in ('Super User','Collection Operator');
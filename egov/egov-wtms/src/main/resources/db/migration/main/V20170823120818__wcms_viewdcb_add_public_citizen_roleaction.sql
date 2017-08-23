
delete from eg_roleaction where actionid = (select id from eg_action where name='viewMeteredConnectionDcb' and contextroot='wtms') and roleid = (select id from eg_role where name='PUBLIC');
 
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('viewMeteredConnectionDcb')));


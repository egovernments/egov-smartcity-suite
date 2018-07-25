
delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and 
actionid = (select id from eg_action where displayname='Create Category' and contextroot='wtms');

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where displayname='Create Category' and contextroot='wtms'));

delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and 
actionid = (select id from eg_action where displayname='Modify Category' and contextroot='wtms');

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where displayname='Modify Category' and contextroot='wtms'));

delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and 
actionid = (select id from eg_action where displayname='Modify Usage Type' and contextroot='wtms');

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where displayname='Modify Usage Type' and contextroot='wtms'));

delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and 
actionid = (select id from eg_action where displayname='Create Usage Type' and contextroot='wtms');

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where displayname='Create Usage Type' and contextroot='wtms'));



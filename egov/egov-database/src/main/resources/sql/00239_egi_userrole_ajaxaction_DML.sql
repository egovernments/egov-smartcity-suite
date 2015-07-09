INSERT INTO eg_action(id, name, CREATEDDATE, LASTMODIFIEDDATE, url, queryparams, PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'ajaxuserlist',  now(),  now(), '/userRole/ajax/userlist', 
null, (SELECT id FROM eg_module WHERE name='User Management'), null, 'ajaxuserlist', false,  'egi');
--rollback delete from eg_action where name='ajaxuserlist' and contextroot='egi';

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='ajaxuserlist'),(Select id from eg_role where name='Grivance Administrator'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='ajaxuserlist'),(Select id from eg_role where name='Super User'));
--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='ajaxuserlist' and contextroot='egi');




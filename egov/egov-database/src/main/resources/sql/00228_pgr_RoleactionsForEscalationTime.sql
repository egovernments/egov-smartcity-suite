update eg_action set name='Define Escalation Time',displayname='Define Escalation Time' where name='Search Escalation Time' and contextroot='pgr';

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Search Escalation Time',  now(),  now(), '/escalationTime/search', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'Search Escalation Time', true,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Search Escalation Time'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Search Escalation Time result',  now(),  now(), '/escalationTime/resultList-update', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'Search Escalation Time result', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Search Escalation Time result'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'get sanction and outsourceposition count',  now(),  now(), '/position/position-getTotalPositionCount', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'get sanction and outsourceposition count', true,  'eis');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='get sanction and outsourceposition count'),(Select id from eg_role where name='Super User'));

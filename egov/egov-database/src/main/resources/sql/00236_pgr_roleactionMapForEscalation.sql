INSERT INTO eg_module(id, name,  enabled,   parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_modulemaster'), 'Escalation',  true,
 (select id from eg_module where name='Pgr Masters'), 'Escalation', 5);
 
INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Search Escalation',  now(),  now(), '/escalation/search-view', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'Search Escalation', true,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Search Escalation'),(Select id from eg_role where name='Super User'));
INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'AjaxEscalationPosition',  now(),  now(), '/complaint/escalation/position', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'AjaxEscalationPosition', false,  'pgr');
--rollback delete from eg_action where name='AjaxEscalationPosition' and context_root='pgr';

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='AjaxEscalationPosition'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'AjaxEscalationBndryType',  now(),  now(), '/complaint/escalation/boundaries-by-type', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'AjaxEscalationBndryType', false,  'pgr');
--rollback delete from eg_action where name='AjaxEscalationBndryType' and context_root='pgr';

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='AjaxEscalationBndryType'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Search Escalation result',  now(),  now(), '/escalation/resultList-update', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'Search Escalation result', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Search Escalation result'),(Select id from eg_role where name='Super User'));


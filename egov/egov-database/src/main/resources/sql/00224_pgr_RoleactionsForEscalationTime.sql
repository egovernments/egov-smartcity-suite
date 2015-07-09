
INSERT INTO eg_module(id, name,  enabled,   parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_modulemaster'), 'Escalation Time',  true,
 (select id from eg_module where name='Pgr Masters'), 'Escalation Time', 4);

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Search Escalation Time',  now(),  now(), '/escalationTime/search-view', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'Search Escalation Time', true,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Search Escalation Time'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'AjaxEscTimeComplaintType',  now(),  now(), '/complaint/escalationTime/complaintTypes', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'AjaxEscTimeComplaintType', false,  'pgr');
--rollback delete from eg_action where name='AjaxEscTimeComplaintType' and context_root='pgr';

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='AjaxEscTimeComplaintType'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'AjaxEscTimeDesignation',  now(),  now(), '/complaint/escalationTime/ajax-approvalDesignations', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'AjaxEscTimeDesignation', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='AjaxEscTimeDesignation'),(Select id from eg_role where name='Super User'));


INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Save Escalation Time',  now(),  now(), '/escalationTime/save-escalationTime', 
null, (SELECT id FROM eg_module WHERE name='Escalation Time'), null, 'Save Escalation Time', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Save Escalation Time'),(Select id from eg_role where name='Super User'));

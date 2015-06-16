
INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'View Escalation',  now(),  now(), '/escalation/view', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'View Escalation', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='View Escalation' and contextroot='pgr'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'Update Escalation',  now(),  now(), '/escalation/update', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'Update Escalation', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Update Escalation' and contextroot='pgr'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'load Designations for escalation',  now(),  now(), '/ajax-designationsByDepartment', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'load Designations for escalation', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='load Designations for escalation' and contextroot='pgr'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot) VALUES (nextval('seq_eg_action'), 'load position for escalation',  now(),  now(), '/ajax-positionsByDepartmentAndDesignation', 
null, (SELECT id FROM eg_module WHERE name='Escalation'), null, 'load position for escalation', false,  'pgr');

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='load position for escalation' and contextroot='pgr'),(Select id from eg_role where name='Super User'));

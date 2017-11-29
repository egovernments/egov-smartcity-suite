------Execute sewerage connection roleaction----=---
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'ExecuteConnectionSearch', '/transactions/connexecutionsearch', null, (select id from eg_module where name = 'SewerageTransactions'), 1, 'Execute Sewerage Connection', true, 'stms', 0, 1, now(), 1, 
now(), (select id from eg_module where name = 'Sewerage Tax Management'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'ExecutionConnectionUpdate', '/transactions/connexecutionupdate', null, (select id from eg_module where name = 'SewerageTransactions'), 1, 'Execute Connection', false, 'stms', 0, 1, now(), 1, 
now(), (select id from eg_module where name = 'Sewerage Tax Management'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AjaxsearchExecution', '/transactions/ajaxsearch', null, (select id from eg_module where name = 'SewerageTransactions'), 1, 'AjaxsearchExecution', false, 'stms', 0, 1, now(), 1, 
now(), (select id from eg_module where name = 'Sewerage Tax Management'));


-----Sewerage Connection Executor role-------

INSERT INTO EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values(nextval('seq_eg_role'),'Sewerage Connection Executor','Sewerage Connection Executor',now(),1,1,now(),0);

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Sewerage Connection Executor'),(select id from eg_action where name='SearchSewerageConnection'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Sewerage Connection Executor'),(select id from eg_action where name='ExecuteConnectionSearch'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Sewerage Connection Executor'),(select id from eg_action where name='AjaxsearchExecution'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Sewerage Connection Executor'),(select id from eg_action where name='ExecutionConnectionUpdate'));
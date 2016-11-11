INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES 
(nextval('seq_eg_module'), 'transactions', true, null, (select id from eg_module where name = 'Council Management'), 'Transactions', 2);

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CreateAgenda', 
'/agenda/new', null, (select id from eg_module where name = 'transactions'), 1,
 'Create Agenda', true, 'council', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Council Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='CreateAgenda'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AgendaAjaxSearch', 
'/agenda/ajaxsearch', null, (select id from eg_module where name = 'transactions'), 1,
 'AgendaAjaxSearch', false, 'council', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Council Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AgendaAjaxSearch'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'createAgenda', 
'/agenda/create', null, (select id from eg_module where name = 'transactions'), 1,
 'createAgenda', false, 'council', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Council Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='createAgenda'));
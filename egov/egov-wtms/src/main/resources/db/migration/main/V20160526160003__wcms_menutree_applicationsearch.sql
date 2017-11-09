INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'Common', true, 'common', null, 'Common',(select max(ordernumber)+1 from eg_module where parentmodule is null));

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'Application', true, NULL, (select id from eg_module where name='Common'), 'Application', 1);

UPDATE eg_action set parentmodule=(select id from eg_module where name='Application'),application =(select id from eg_module where name='Common')
where id in (select id from eg_action where url='/elastic/appSearch/');

UPDATE eg_action set parentmodule=(select id from eg_module where name='Application') ,application =(select id from eg_module where name='Common')
where id in (select id from eg_action where url='/elastic/appSearch/ajax-moduleTypepopulate');

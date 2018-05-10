
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname) 
VALUES (nextval('SEQ_EG_MODULE'), 'Notifications', true, 'eventnotification', (select id from eg_module where name = 'Event Notification'), 'Notifications');

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Drafts','/drafts/create/',(select id from eg_module where name='Notifications' ),1,
'Draft',false,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'View Drafts','/drafts/view/',(select id from eg_module where name='Notifications' ),1,
'Draft',true,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Drafts'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='View Drafts'));


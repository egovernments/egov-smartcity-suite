update eg_module set enabled=false where name='Event';

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Event','/event/new/',(select id from eg_module where name='Event Notification' ),1,
'Event',true,'eventnotification',(select id from eg_module where name='Event Notification' and parentmodule is null));

delete * from eg_action where name='View Event';

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Event'));

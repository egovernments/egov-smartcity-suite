Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Update Drafts','/drafts/update/',(select id from eg_module where name='Notifications' ),1,
'Draft',false,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Update Drafts'));
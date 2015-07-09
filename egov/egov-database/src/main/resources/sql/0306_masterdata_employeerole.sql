insert into eg_role(id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version) values 
(nextval('seq_eg_role'),'Employee','Default role for all employees',current_date,1,1,current_date,0);


insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='Inbox'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='InboxDraft'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='InboxHistory'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='Role View'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='Role Update'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialsProfileEdit'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialSentFeedBack'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialChangePassword'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='AddFavourite'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='Employee'),(select id from eg_action where name='RemoveFavourite'));


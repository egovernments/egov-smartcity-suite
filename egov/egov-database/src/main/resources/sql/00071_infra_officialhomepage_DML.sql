insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/inbox'),(Select id from eg_role where name='Super User'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/inbox/draft'),(Select id from eg_role where name='Super User'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/inbox/history'),(Select id from eg_role where name='Super User'));
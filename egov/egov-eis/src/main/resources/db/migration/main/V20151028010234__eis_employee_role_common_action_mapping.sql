insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='Role View'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialChangePassword'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='RemoveFavourite'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialsProfileEdit'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='Role Update'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='AddFavourite'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee'),(select id from eg_action where name='OfficialSentFeedBack'));

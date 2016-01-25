Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='DonationMasterDetailsScreen'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ChairPersonDetailsScreen'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='WaterRatesDetailsMaster'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='CategoryMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='DocumentNamesMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ApplicationProcessTimeMaster'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ajaxapplicationprocesstime'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='categorytypebypropertytypeajax'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='usagetypebypropertytypeajax'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='pipesizesbypropertytypeajax'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='minimumpipesizeajax'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='maximumpipesizeajax'));
----------------------------------- PTIS Reports Role Action for ERP Report Viewer-------------------------------------

------------------------------------Revenue Ward Wise Collection Report------------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Revenue Ward Wise Collection Report' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Collection Summary Report Result' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));

--------------------------------------------------Defaulters Report----------------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Defaulters Report' and contextroot='ptis'),
(select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'defaultersReportResult' and contextroot='ptis'), 
(select id from eg_role where name in ('ERP Report Viewer')));

---------------------------------------------------Arrear Register Report----------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Arrears Register Report' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'GenerateArrearRegisterReport' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'ajaxLoadBoundaryBlock' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'ajaxLoadBlockByWard'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Load Block By Locality'), (select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'ajaxLoadBoundary' and contextroot='ptis'), (select id from eg_role where name in ('ERP Report Viewer')));

--------------------------------------------------DCB Report-----------------------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'SearchDCBReport' and contextroot='ptis'),
(select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'DCB Report' and contextroot='ptis'),
(select id from eg_role where name in ('ERP Report Viewer')));

--------------------------------------------------Base Register Report-------------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Base Register' and contextroot='ptis'),
(select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Base Register Report result' and contextroot='ptis'), 
(select id from eg_role where name in ('ERP Report Viewer')));

--------------------------------------------------Daily Collection Report----------------------------------------------

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Daily collection report' and contextroot='ptis'), 
(select id from eg_role where name in ('ERP Report Viewer')));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Daily collection report result' and contextroot='ptis'), 
(select id from eg_role where name in ('ERP Report Viewer')));
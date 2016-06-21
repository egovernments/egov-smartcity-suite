
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Agencywise Collection Report'), id from eg_role where name in ('ERP Report Viewer');
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Agency Wise DCB Report'), id from eg_role where name in ('ERP Report Viewer');
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'ActiveAgencyAjaxDropdown'), id from eg_role where name in ('ERP Report Viewer');
INSERT into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Agency Hoardings'), id from eg_role where name in ('ERP Report Viewer');

INSERT into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AjaxSubCategories'), id from eg_role where name in ('ERP Report Viewer');
INSERT into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Load Block By Locality'), id from eg_role where name in ('ERP Report Viewer');
INSERT into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'dcbReportSearch'), id from eg_role where name in ('ERP Report Viewer');
INSERT into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'HoardingDcbReport'), id from eg_role where name in ('ERP Report Viewer');



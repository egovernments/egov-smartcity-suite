Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='WaterTaxDCBReportWardWise'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='WaterTaxDCBReportLocalityWise'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='DefaultersReport'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='DefaultersReport-ajax'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='DailyWTCollectionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name= 'ERP Report Viewer'),(select id FROM eg_action WHERE NAME = 'WaterTaxConnectionReportWardWise' and CONTEXTROOT='wtms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'BaseRegister Report' and contextroot = 'wtms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'BaseRegister Report result' and contextroot = 'wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'ERP Report Viewer'),(select id FROM eg_action WHERE NAME = 'WaterTaxConnectionReportWardWiseupdate' and CONTEXTROOT='wtms'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='DCBReportList' and CONTEXTROOT='wtms'));




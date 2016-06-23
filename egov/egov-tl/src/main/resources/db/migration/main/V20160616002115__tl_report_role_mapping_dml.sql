insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'SearchTradeLicense' and contextroot = 'tl'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'TradeLicenseDCBReportLocalityWise' and contextroot = 'tl'));

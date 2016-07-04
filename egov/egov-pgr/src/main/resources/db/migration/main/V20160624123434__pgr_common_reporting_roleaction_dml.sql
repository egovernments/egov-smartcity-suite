insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Complaint Type Wise Report' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Ageing Report By Department wise' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Ageing Report By Boundary wise' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Drill Down Report By Boundary wise' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Drill Down Report By Department wise' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'SearchComplaintFormOfficial' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Drill Down Report search result' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Ageing report search result' and contextroot = 'pgr'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'Complaint Type Wise Report search result' and contextroot = 'pgr'));


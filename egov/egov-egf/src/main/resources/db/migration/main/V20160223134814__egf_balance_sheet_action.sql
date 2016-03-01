

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'BalanceSheetReport-print','/report/balanceSheetReport-printBalanceSheetReport.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='BalanceSheetReport-print'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BalanceSheetReport-print'));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-balancesheet'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-balancesheet-showdropdown'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-balancesheetsubreport-showdropdown'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-BSSpdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-BSSXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-BSpdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-BSXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-genSchepdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-BSreport-genScheXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-bs-detailedschedule'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-bs-detailedschedulepdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-bs-detailedschedulexls'));







update eg_action set displayname='Modify Chart Of Accounts' where name='ChartOfAccountsViewModify';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ChartOfAccountsView','/eGov_viewCOA.jsp','window=left',
(select id from eg_module where name='Chart Of Accounts Tree'),
3,'View Chart Of Accounts','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsView'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccountsView'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ModifyChartOfAccounts','/masters/chartOfAccounts-modifyChartOfAccounts.action',null,
(select id from eg_module where name='Chart Of Accounts Tree'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ModifyChartOfAccounts'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ModifyChartOfAccounts'));

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Chart Of Accounts'), id from eg_action where name  in('ChartOfAccountsView','ModifyChartOfAccounts');

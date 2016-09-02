
insert into eg_module values(nextval('seq_eg_module'),'Chart Of Accounts Tree',true,null,(select id from eg_module  where name='Chart of Accounts'),'Chart Of Accounts Tree',1);

update eg_module set ordernumber=2 where name='Detailed Code';

update eg_action set parentmodule =(select id from eg_module where name='Chart Of Accounts Tree'),displayname='Create Chart Of Accounts' where 
name='Chart Of Accounts';

update eg_action set displayname='Create Detailed Code' where name='exil-chartofaccount add new';
update eg_action set displayname='Modify Detailed Code' where name='ChartOfAccounts-editDetailedCode';
update eg_action set displayname='View Detailed Code' where name='ChartOfAccounts-viewDetailedCode';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ChartOfAccountsViewModify','/eGov_viewAndModifyCOA.jsp','window=left',
(select id from eg_module where name='Chart Of Accounts Tree'),
2,'View/Modify Chart Of Accounts','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccountsViewModify'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccountsViewModify'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ViewChartOfAccounts','/masters/chartOfAccounts-viewChartOfAccounts.action',null,
(select id from eg_module where name='Chart Of Accounts Tree'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ViewChartOfAccounts'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ViewChartOfAccounts'));

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Chart Of Accounts'), id from eg_action where name  in('ChartOfAccountsViewModify','ViewChartOfAccounts');

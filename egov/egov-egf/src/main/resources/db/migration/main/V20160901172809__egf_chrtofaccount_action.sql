
delete from eg_feature_action where action =(select id from eg_action where name ='Detailed Code-Create/Modify/View');
delete from eg_roleaction  where actionid =(select id from eg_action where name='Detailed Code-Create/Modify/View');
delete from eg_action where name='Detailed Code-Create/Modify/View';

insert into eg_module values(nextval('seq_eg_module'),'Detailed Code',true,null,(select id from eg_module  where name='Chart of Accounts'),'Detailed Code',1);

update eg_action set enabled =true,displayname='Create Chart Of Accounts',parentmodule=(select id from eg_module  where name='Detailed Code'),
ordernumber=1 where name='exil-chartofaccount add new';

update eg_action set displayname ='Chart Of Accounts Tree' where name='Chart Of Accounts';



Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ChartOfAccounts-editDetailedCode','/masters/chartOfAccounts-editDetailedCode.action',null,
(select id from eg_module where name='Detailed Code'),
2,'Modify Chart Of Accounts','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccounts-editDetailedCode'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccounts-editDetailedCode'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ChartOfAccounts-viewDetailedCode','/masters/chartOfAccounts-viewDetailedCode.action',null,
(select id from eg_module where name='Detailed Code'),
3,'View Chart Of Accounts','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ChartOfAccounts-viewDetailedCode'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ChartOfAccounts-viewDetailedCode'));


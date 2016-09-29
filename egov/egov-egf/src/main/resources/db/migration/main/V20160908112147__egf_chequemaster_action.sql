
insert into eg_module values(nextval('seq_eg_module'),'Bank Account Cheque',true,null,(select id from eg_module  where name='Chart of Accounts'),'Bank Account Cheque',3);

update eg_action set displayname ='Add/Modify Cheque',parentmodule=(select id from eg_module where name='Bank Account Cheque'),ordernumber=1
where name='Add/Modify Bank Account Cheque';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'accountCheque-view','/masters/accountCheque-view.action',null,
(select id from eg_module where name='Bank Account Cheque'),
2,'View Cheque','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='accountCheque-view'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='accountCheque-view'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'accountCheque-viewCheques','/masters/accountCheque-viewCheques.action',null,
(select id from eg_module where name='Bank Account Cheque'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='accountCheque-viewCheques'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='accountCheque-viewCheques'));

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Add/Modify Bank Account Cheque'), id from eg_action where name  in('accountCheque-view','accountCheque-viewCheques'); 

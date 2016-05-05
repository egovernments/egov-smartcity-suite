Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittancePrintBankChallan','/receipts/bankRemittance-printBankChallan.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Remittance Print Bank Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallan'));


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittancePrintBankChallanReport','/reports/remittanceStatementReport-printBankChallan.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Remittance Print Bank Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallanReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallanReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallanReport'));


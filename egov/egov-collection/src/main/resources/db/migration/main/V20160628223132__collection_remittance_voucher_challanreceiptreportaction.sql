INSERT into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application")
values
(nextval('seq_eg_action'),'RemittanceVoucherReceiptReport','/reports/remittanceVoucherReport-reportReceiptDetails.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Remittance Voucher Receipt Report','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReceiptReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReceiptReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReceiptReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReceiptReport'));


INSERT into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittanceReportPrintBankChallan','/reports/remittanceStatementReport-reportPrintBankChallan.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Remittance Report Print Bank Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceReportPrintBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceReportPrintBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceReportPrintBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceReportPrintBankChallan'));


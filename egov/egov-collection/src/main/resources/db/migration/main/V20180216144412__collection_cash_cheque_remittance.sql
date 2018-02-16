INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'),'Collection Remittance',true,null,(select id from eg_module where name='Collection'),'Remittance',4);

update eg_action set displayname= 'Cash Remittance',parentmodule=(select id from eg_module where name='Collection Remittance'),enabled=false where name='BankRemittance' and contextroot='collection';

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeRemittance','/receipts/chequeRemittance-newform.action',null,(select id from eg_module where name='Collection Remittance'),2,'Cheque Remittance',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='ChequeRemittance'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ChequeRemittance'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ChequeRemittance'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ChequeRemittance'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeRemittanceListData','/receipts/chequeRemittance-listData',null,(select id from eg_module where name='Collection Remittance'),2,'ChequeRemittanceListData',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='ChequeRemittanceListData'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ChequeRemittanceListData'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ChequeRemittanceListData'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ChequeRemittanceListData'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeRemittanceCreate','/receipts/chequeRemittance-create',null,(select id from eg_module where name='Collection Remittance'),2,'ChequeRemittanceCreate',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='ChequeRemittanceCreate'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ChequeRemittanceCreate'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ChequeRemittanceCreate'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ChequeRemittanceCreate'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeRemittancePrintBankChallan','/receipts/chequeRemittance-printBankChallan.action',null,(select id from eg_module where name='Collection Remittance'),2,'ChequeRemittancePrintBankChallan',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='ChequeRemittancePrintBankChallan'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ChequeRemittancePrintBankChallan'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ChequeRemittancePrintBankChallan'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ChequeRemittancePrintBankChallan'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemittancePrintChequeBankChallan','/reports/remittanceStatementReport-printChequeBankChallan.action',null,(select id from eg_module where name='Collection Reports'),3,'RemittancePrintChequeBankChallan',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='SYSTEM'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintChequeBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintChequeBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintChequeBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='BANK COLLECTION REMITTER'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintChequeBankChallan'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemittancePrintCashBankChallan','/reports/remittanceStatementReport-printCashBankChallan.action',null,(select id from eg_module where name='Collection Reports'),3,'RemittancePrintCashBankChallan',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='SYSTEM'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintCashBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintCashBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintCashBankChallan'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='BANK COLLECTION REMITTER'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintCashBankChallan'));


Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ReceiptHeader','Partial Remitted',now(),'PARTIAL_REMITTED',1);


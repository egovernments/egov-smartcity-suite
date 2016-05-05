Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittanceVoucherReport','/reports/remittanceVoucherReport-criteria.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Remittance Voucher Report','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittanceVoucherReportResult','/reports/remittanceVoucherReport-report.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'RemittanceVoucherReportResult','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittanceStatementReport','/reports/remittanceStatementReport-criteria.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'Bank Remittance Statement','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReport'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") 
values
(nextval('seq_eg_action'),'RemittanceStatementReportResult','/reports/remittanceStatementReport-report.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,
'RemittanceStatementReportResult','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Super User'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReportResult'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ULB Operator'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReportResult'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Remitter'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReportResult'));


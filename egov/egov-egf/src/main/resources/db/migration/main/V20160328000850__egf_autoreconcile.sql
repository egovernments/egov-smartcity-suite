update   eg_action set enabled=true where url in('/brs/autoReconciliation-beforeUpload.action','/brs/autoReconciliation-new.action');

update eg_action set url='/brs/autoReconciliation-upload.action' where url='/brs/autoReconciliation.action';

  CREATE TABLE  EGF_BRS_BANKSTATEMENTS 																			
   (	ID BigInt, 
	ACCOUNTNUMBER VARCHAR(20), 
	ACCOUNTID BigInt, 
	TXDATE DATE, 
	TYPE VARCHAR(64  ), 
	INSTRUMENTNO VARCHAR(8  ), 
	DEBIT numeric(20,2), 
	CREDIT numeric(20,2), 
	BALANCE numeric(20,2), 
	NARRATION VARCHAR(126  ), 
	CSLNO VARCHAR(32  ), 
	CREATEDDATE DATE, 
	ACTION VARCHAR(32  ), 
	RECONCILIATIONDATE DATE, 
	ERRORCODE VARCHAR(8  ), 
	ERRORMESSAGE VARCHAR(256  ), 
	 CONSTRAINT PK_EGF_BRS_BANKSTATEMENTS PRIMARY KEY (ID)
	 );
CREATE SEQUENCE seq_egf_brs_bankstatements;


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'autoReconciliation-generateReport','/brs/autoReconciliation-generateReport.action',(select id from eg_module where name='BRS' ),
1,'autoReconciliation-generateReport',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'autoReconciliation-schedule','/brs/autoReconciliation-schedule.action',(select id from eg_module where name='BRS' ),
1,'autoReconciliation-schedule',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'autoReconciliation-generatePDF','/brs/autoReconciliation-generatePDF.action',(select id from eg_module where name='BRS' ),
1,'autoReconciliation-generatePDF',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'autoReconciliation-generateXLS','/brs/autoReconciliation-generateXLS.action',(select id from eg_module where name='BRS' ),
1,'autoReconciliation-generateXLS',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));


Insert into eg_roleaction (select (select id from eg_role where name='Super User'), 
id from eg_action where url like '/brs/autoReconciliation%');

update fund set version=0;





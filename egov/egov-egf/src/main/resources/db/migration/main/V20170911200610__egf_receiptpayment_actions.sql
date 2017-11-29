
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View ReceiptPayment','/receiptpayment/search',(select id from eg_module where name='Financial Statements' and parentmodule=(select id from eg_module where name='Reports')),2,'Receipt Payment Report',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='View ReceiptPayment'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='View ReceiptPayment'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-ReceiptPayment','/receiptpayment/ajaxsearch',(select id from eg_module where name='Financial Statements' and parentmodule=(select id from eg_module where name='Reports')),1,'Search and View Result-ReceiptPayment',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Search and View Result-ReceiptPayment'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Search and View Result-ReceiptPayment'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View ReceiptPayment','View ReceiptPayment',(select id from eg_module  where name = 'EGF'));


INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View ReceiptPayment'), id from eg_action where name  in('View ReceiptPayment','Search and View Result-ReceiptPayment');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View ReceiptPayment'),id from eg_role where name in('SYSTEM','Financial Report Viewer');

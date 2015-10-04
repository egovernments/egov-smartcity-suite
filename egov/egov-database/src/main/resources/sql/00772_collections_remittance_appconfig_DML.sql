INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'CREATEVOUCHER_FOR_REMITTANCE','Create Voucher for Remittance',(select id from eg_module where name='Collection'));
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='CREATEVOUCHER_FOR_REMITTANCE'),current_date,'N');

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD','Remittance Voucher Type for Cheque,DD and Card',(select id from eg_module where name='Collection'));
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD'),current_date,'Contra');

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'USERECEIPTDATEFORCONTRA','Use Receipt Voucher Date for Contra Voucher',(select id from eg_module where name='Collection'));
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='USERECEIPTDATEFORCONTRA'),current_date,'N');

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ReceiptHeader','Remitted',to_date('23-01-10','DD-MM-RR'),'REMITTED',6);





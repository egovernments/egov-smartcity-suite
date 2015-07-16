
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'BILLINGSERVICEPAYMENTGATEWAY','Get_Billing_Service_Payment_Gateway',(select id from eg_module where name='Collection'));


Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='BILLINGSERVICEPAYMENTGATEWAY'),to_date('23-09-09','DD-MM-RR'),'PT|AXIS',0);


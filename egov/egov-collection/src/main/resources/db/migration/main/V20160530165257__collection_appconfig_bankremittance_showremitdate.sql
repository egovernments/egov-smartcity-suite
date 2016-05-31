INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'COLLECTION_BANKREMITTANCE_SHOWREMITDATE','BankRemittance Screen show date of remittance',(select id from eg_module where name='Collection'));
INSERT into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='COLLECTION_BANKREMITTANCE_SHOWREMITDATE'),current_date,'N');



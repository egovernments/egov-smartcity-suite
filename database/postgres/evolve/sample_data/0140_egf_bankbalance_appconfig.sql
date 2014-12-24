#UP
INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
seq_eg_appconfig_values.nextval, (select id from EG_APPCONFIG where key_name='bank_balance_mandatory'), sysdate, 'Y'); 

#DOWN
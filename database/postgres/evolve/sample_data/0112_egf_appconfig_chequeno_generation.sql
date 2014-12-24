#UP

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
seq_eg_appconfig_values.nextval, (select id from EG_APPCONFIG where key_name='Cheque_no_generation_auto'),  TO_Date( '12/17/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'Y'); 

#DOWN
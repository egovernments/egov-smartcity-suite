
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'CANCEL_RECEIPT_FOR_CLOSED_MUTATION', 'To cancel the mutation fee receipt if mutation completed',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CANCEL_RECEIPT_FOR_CLOSED_MUTATION'), current_date,false,0);



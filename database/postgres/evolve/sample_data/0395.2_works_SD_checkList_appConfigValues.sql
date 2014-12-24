#UP
INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Return-Security-Deposit-CheckList'),  SYSDATE, 'Whether there are any liabilities against the contractor/supplier for this work?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Return-Security-Deposit-CheckList'),  SYSDATE, 'Whether work is in satisfactory condition as on today?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Return-Security-Deposit-CheckList'),  SYSDATE, 'Whether all debris are removed and site is clear as on today?');


#DOWN
DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Return-Security-Deposit-CheckList');

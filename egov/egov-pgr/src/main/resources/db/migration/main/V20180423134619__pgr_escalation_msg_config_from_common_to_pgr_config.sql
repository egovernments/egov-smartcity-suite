DELETE FROM eg_appconfig_values WHERE KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFORESCALATION');

DELETE FROM eg_appconfig WHERE key_name='SENDEMAILFORESCALATION';

INSERT INTO egpgr_configuration VALUES (nextval('seq_egpgr_configuration'), 'SEND_MESSAGE_ON_ESCALATION', 'true',
'Send Email and SMS alert while complaint escalation',1,now(),1,now(),0);

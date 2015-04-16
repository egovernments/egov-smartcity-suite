INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) 
VALUES (nextval('seq_eg_appconfig'), 'smsSender', 'Sms sender phone number or name', 'egi'); 


INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) 
VALUES (nextval('seq_eg_appconfig'), 'serviceProviderUrl', 'Service Provider Url for sending sms', 'egi'); 


--rollback DELETE FROM EG_APPCONFIG WHERE KEY_NAME='smsSender';
--rollback DELETE FROM EG_APPCONFIG WHERE KEY_NAME='serviceProviderUrl';


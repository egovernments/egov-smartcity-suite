

delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='DemandReasonGlcodeMap');

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTAXDONATION=1100201'
,0);


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTAXSECURITY=1100201'
,0);
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTAXFIELDINSPEC=1407011'
,0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTAXCHARGES=1405016'
,0);
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTADVANCE=3504106'
,0);



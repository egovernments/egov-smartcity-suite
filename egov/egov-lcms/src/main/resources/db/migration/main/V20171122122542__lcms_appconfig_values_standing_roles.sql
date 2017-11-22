---Update role for standing counsel

update eg_role set name='STANDING_COUNSEL' where name='TP Standing Counsel';

----App config values for business roles
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'STANDINGCOUNSEL_ROLES', 'For these Role create business users as standing counsel  ',0, (select id from eg_module where name='LCMS'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='STANDINGCOUNSEL_ROLES'), current_date, 'EMPLOYEE',0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='STANDINGCOUNSEL_ROLES'), current_date, 'STANDING_COUNSEL',0);
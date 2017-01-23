INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Trade license Act corporation','Trade license act for corporation ',(select id from eg_module where name='Trade License'));

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Trade license Act Muncipalities','Trade license act for Muncipalities ',(select id from eg_module where name='Trade License'));


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Trade license Act corporation' AND MODULE =(select id from eg_module where name='Trade License')),current_date,'(Issued u/s 521 & 622 of A.P.Municipal Corporations Act, 1994 (formerly GHMC Act, 1955)',0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Trade license Act Muncipalities' AND  MODULE =(select id from eg_module where name='Trade License')),current_date,'(Issued u/s 263,264,272,275 & 288 etc., of A.P.Municipalities Act, 1965)',0);

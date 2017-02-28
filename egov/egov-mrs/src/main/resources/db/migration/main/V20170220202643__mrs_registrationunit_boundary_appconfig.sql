INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_REGISTRATIONUNIT_HEIRARCHYTYPE', 'Marriage registrationunit heirarchytype',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_REGISTRATIONUNIT_HEIRARCHYTYPE' and module= (select id from eg_module where name='Marriage Registration')), current_date, 'ADMINISTRATION',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_REGISTRATIONUNIT_BOUNDARYYTYPE', 'Marriage registrationunit boundarytype',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_REGISTRATIONUNIT_BOUNDARYYTYPE' and module= (select id from eg_module where name='Marriage Registration')), current_date, 'City',0);

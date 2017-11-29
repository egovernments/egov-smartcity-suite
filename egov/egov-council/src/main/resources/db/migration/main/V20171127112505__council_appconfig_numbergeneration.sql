INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES(nextval('SEQ_EG_APPCONFIG'), 'AGENDA_NUMBER_AUTO', 'Automatic Agenda number generation',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='AGENDA_NUMBER_AUTO'AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MEETING_NUMBER_AUTO', 'Automatic Meeting number generation',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MEETING_NUMBER_AUTO' AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'RESOLUTION_NUMBER_AUTO', 'Automatic Resolution number generation',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RESOLUTION_NUMBER_AUTO' AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PREAMBLE_NUMBER_AUTO', 'Automatic Preamble number generation',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PREAMBLE_NUMBER_AUTO' AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);

INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'checkUnqAgendaNumber','/councilmom/checkUnique-agendaNo',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Unique Agenda number check',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='checkUnqAgendaNumber'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='checkUnqAgendaNumber'));

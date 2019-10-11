------ Appconfig to enable/disable Bulk Bill Generation Scheduler
INSERT INTO eg_appconfig ( ID, KEYNAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PTIS_BULKBILL_GENERATION_SCHEDULER_ENABLED', 'Key to enable/disable bulk bill generation scheduler for Property Tax',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, CONFIG, EFFECTIVEFROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEYNAME='PTIS_BULKBILL_GENERATION_SCHEDULER_ENABLED'), current_date,'N',0);


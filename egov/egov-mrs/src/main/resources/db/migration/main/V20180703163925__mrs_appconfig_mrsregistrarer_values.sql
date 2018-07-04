----------------------------- app config value to identify MRSRegistrar Reassignment for MRS-----------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'MRSDESIGNATIONFORMRSREGISTRAR', 'Designation for MRSRegistrar Workflow',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDESIGNATIONFORMRSREGISTRAR'), current_date, 'Commissioner,Additional Commissioner,Deputy Commissioner,Zonal Commissioner,Assistant Commissioner, Chief Medical Officer Health,Assistant Medical Officer Health,Municipal Health Officer',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MRSDEPARTMENTFORMRSREGISTRARAR', 'Department for MRSRegistrar Workflow',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDEPARTMENTFORMRSREGISTRARAR'), current_date, 'Administration,Public Health Department',0);
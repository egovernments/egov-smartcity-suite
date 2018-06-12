------ Appconfig to enable/disable demand voucher generation
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PTIS_DEMAND_VOUCHER_GENERATION_REQUIRED', 'Key to enable/disable demand voucher generation for Property Tax',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PTIS_DEMAND_VOUCHER_GENERATION_REQUIRED'), current_date,'N',0);



------ Appconfig for Demand Voucher Glcodes
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PT_DEMAND_VOUCHER_GLCODES', 'Glcodes used in demand voucher generation for Property Tax',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values (ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PT_DEMAND_VOUCHER_GLCODES'), current_date,'CURR_DMD=4311001~ARR_DMD=4311004~ADVANCE=3504101~GEN_TAX=1100101~VAC_LAND_TAX=1100102~LIB_CESS=3503088',0);



------ Updating department code to REV 
update eg_appconfig_values set value='REV' where key_id=(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DEPARTMENT_CODE' and module = (select id from eg_module where name='Property Tax'));

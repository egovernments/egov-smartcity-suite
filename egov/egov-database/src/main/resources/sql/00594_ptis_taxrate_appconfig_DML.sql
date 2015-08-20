delete from eg_demand_reason_details where id_demand_reason in (select id from eg_demand_reason where id_demand_reason_master in (select id from eg_demand_reason_master where module=(select id from eg_module where name='Property Tax')));

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PT_TAXRATES', 'The tax rates for each tax head applicable for ULB',0, (select id from eg_module where name='Property Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PT_TAXRATES'), current_date, 'GEN_TAX_RESD=20'||chr(10)||'GEN_TAX_NR=25'||chr(10)||'VAC_LAND_TAX=0.2'||chr(10)||'LIB_CESS=4'||chr(10)||'EDU_CESS=8'||chr(10)||'SEW_TAX_RESD=0'||chr(10)||'SEW_TAX_NR=0'||chr(10)||'PRIMARY_SER_CHRG=0',0);

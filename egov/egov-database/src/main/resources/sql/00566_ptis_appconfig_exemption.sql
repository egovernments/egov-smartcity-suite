alter table egpt_floor_detail drop column TAX_EXEMPTED_REASON;
alter table egpt_property drop column isexemptedfromtax;
alter table egpt_property add column isexemptedfromtax boolean default false;
alter table egpt_property drop column taxexemptreason;
ALTER TABLE egpt_property ADD COLUMN tax_exempted_reason BIGINT;
ALTER TABLE egpt_property ADD CONSTRAINT fk_exemption_reason FOREIGN KEY(tax_exempted_reason) REFERENCES egpt_exemption_reason(id);
update eg_demand_reason_master set reasonmaster='Primary Service Charges', code='PRIMARY_SER_CHRG' where reasonmaster='Public Service Charges' and code='PUB_SER_CHRG';
update egpt_occupation_type_master set code='TENANT' where code='TANANT';
update egpt_property_type_master set property_type='Central Government 75%', code='CENTRAL_GOVT_75' where code ='CENTRAL_GOVT_70';
update egpt_property_type_master set property_type='State Government', code='STATE_GOVT' where code ='CENTRAL_GOVT_33.5' and orderno=3;

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'IS_CORPORATION', 'To know the ULB is Corporation or not',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'IS_SEASHORE_ULB', 'To know the ULB is on Sea Shore or not',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'IS_PRIMARYSERVICECHARGES_APPLICABLE', 'To know the Primary Service Charges are applicable in ULB or not',0, (select id from eg_module where name='Property Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='IS_CORPORATION'), current_date, '0',0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='IS_SEASHORE_ULB'), current_date, '0',0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='IS_PRIMARYSERVICECHARGES_APPLICABLE'), current_date, '0', 0);

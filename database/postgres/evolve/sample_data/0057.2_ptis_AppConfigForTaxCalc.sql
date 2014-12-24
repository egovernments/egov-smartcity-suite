#UP

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'PTIS_VACANT_DMD_CALC_BAREA','Property Tax Vacant Land Area','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'PTIS_VACANT_DMD_CALC_BAREA'),to_date('01/04/2009','dd/MM/yyyy'),'2400');

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'PTIS_VACANT_DMD_CALC_BAREA_RATE','Property Tax Vacant Land Area Rate','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'PTIS_VACANT_DMD_CALC_BAREA_RATE'),to_date('01/04/2009','dd/MM/yyyy'),'50');

#DOWN

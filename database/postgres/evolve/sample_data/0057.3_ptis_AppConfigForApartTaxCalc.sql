#UP

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'FLATSINMULTI_CATEGORY_FACTOR','Flats Category Factory','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'FLATSINMULTI_CATEGORY_FACTOR'),to_date('01/04/2009','dd/MM/yyyy'),'1');

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'TAX_PERC','Property Tax Vacant Land Area Rate','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'TAX_PERC'),to_date('01/04/2009','dd/MM/yyyy'),'10.92');

#DOWN
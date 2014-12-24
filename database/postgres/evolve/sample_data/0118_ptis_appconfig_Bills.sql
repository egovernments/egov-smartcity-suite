#UP

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'FUNCTIONARY_CODE','Functionary code','Property Tax');
insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'FUND_CODE','Fund Code','Property Tax');
insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'FUND_SOURCE_CODE','FundSource Code','Property Tax');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'FUNCTIONARY_CODE'),to_date('01/04/2009','dd/MM/yyyy'),'01');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'FUND_CODE'),to_date('01/04/2009','dd/MM/yyyy'),'01');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'FUND_SOURCE_CODE'),to_date('01/04/2009','dd/MM/yyyy'),'85');

#DOWN

#UP
insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'COLLECTION_MODES','All Collection Codes','Property Tax');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'COLLECTION_MODES'),to_date('01/04/2009','dd/MM/yyyy'),'450100000,450250000');

#DOWN


delete from eg_APPCONFIG_values where key_id in(select id from eg_appconfig where key_name='COLLECTION_MODES' and MODULE='Property Tax');

delete from eg_appconfig where key_name = 'COLLECTION_MODES' and MODULE='Property Tax';

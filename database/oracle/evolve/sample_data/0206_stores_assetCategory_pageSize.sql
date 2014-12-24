#UP
insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'ASSETCATEGORY_PAGINATION_PAGESIZE',' pagination page size for asset category','STORES');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'ASSETCATEGORY_PAGINATION_PAGESIZE'),to_date('01/04/2009','dd/MM/yyyy'),10);

#DOWN


delete from eg_APPCONFIG_values where key_id in(select id from eg_appconfig where key_name='ASSETCATEGORY_PAGINATION_PAGESIZE' and MODULE='STORES');

delete from eg_appconfig where key_name = 'ASSETCATEGORY_PAGINATION_PAGESIZE' and MODULE='STORES';

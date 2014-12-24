#UP
insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'STORES_PAGINATION_REPORT_PAGESIZE',' pagination page size for report','STORES');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'STORES_PAGINATION_REPORT_PAGESIZE'),to_date('22/06/2010','dd/MM/yyyy'),20);

#DOWN


delete from eg_APPCONFIG_values where key_id in(select id from eg_appconfig where key_name='STORES_PAGINATION_REPORT_PAGESIZE' and MODULE='STORES');

delete from eg_appconfig where key_name = 'STORES_PAGINATION_REPORT_PAGESIZE' and MODULE='STORES';

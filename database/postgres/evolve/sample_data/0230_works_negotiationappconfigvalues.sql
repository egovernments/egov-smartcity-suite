#UP

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='TENDER_NEGOTIATION_CREATED_BY_SELECTION'),to_date('03/05/2010','dd/MM/yyyy'),'no');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='TENDER_NEGOTIATION_DATE_SELECTION'),to_date('03/05/2010','dd/MM/yyyy'),'yes');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WORKS_PACKAGE_STATUS'),to_date('03/05/2010','dd/MM/yyyy'),'Approved');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='ESTIMATE_STATUS'),to_date('03/05/2010','dd/MM/yyyy'),'Admin Sanctioned');


#DOWN


DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='TENDER_NEGOTIATION_CREATED_BY_SELECTION');

DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='TENDER_NEGOTIATION_DATE_SELECTION');

DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WORKS_PACKAGE_STATUS');

DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='ESTIMATE_STATUS');



 



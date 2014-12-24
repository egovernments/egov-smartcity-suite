#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorksPackage.laststatus'),
to_date('03/05/2010','dd/MM/yyyy'),'L1 tender finalised');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorksPackage.setstatus'),
to_date('03/05/2010','dd/MM/yyyy'),'Noticeinvitingtenderreleased,Tender document released,Tender opened,Technical Evaluation done,Commercial Evaluation done,L1 tender finalised');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WorksPackage.setstatus');
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WorksPackage.laststatus');


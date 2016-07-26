
insert into EG_APPCONFIG_VALUES (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),
(select id from EG_APPCONFIG where KEY_NAME ='DataEntryCutOffDate'),current_date,'01-Jul-2016',0);

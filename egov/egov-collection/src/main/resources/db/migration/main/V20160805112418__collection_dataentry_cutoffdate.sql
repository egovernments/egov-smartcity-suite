Insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,MODULE) values (nextval('seq_eg_appconfig'),'COLLECTIONDATAENTRYCUTOFFDATE',
'Collection Data entry cut off date',0,(select id from eg_module where name='Collection'));

Insert into EG_APPCONFIG_VALUES (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),
(select id from EG_APPCONFIG where KEY_NAME ='COLLECTIONDATAENTRYCUTOFFDATE'),current_date,'31/07/2016',0);



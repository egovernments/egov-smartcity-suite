insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,createdby,createddate,MODULE) values (nextval('seq_eg_appconfig'),'allow_billsaccounting_across_years',
'Allow bills accounting accross years',0,1,current_date,(select id from eg_module where name='EGF'));

insert into EG_APPCONFIG_VALUES (ID,KEY_ID,EFFECTIVE_FROM,VALUE,createddate,createdby,VERSION) values (nextval('seq_eg_appconfig_values'),
(select id from EG_APPCONFIG where KEY_NAME ='allow_billsaccounting_across_years'),current_date,'yes',current_date,1,0);


insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,createdby,createddate,MODULE) values (nextval('seq_eg_appconfig'),'billsaccounting_accrossyears_enddate',
'Allow bills accounting accross years end date',0,1,current_date,(select id from eg_module where name='EGF'));


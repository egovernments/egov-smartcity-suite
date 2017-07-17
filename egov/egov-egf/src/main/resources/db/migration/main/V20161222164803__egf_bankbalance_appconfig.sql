insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,CREATEDBY,CREATEDDATE,MODULE) 
  values (nextval('seq_eg_appconfig'),'Balance Check Control Type', 'Balance check control type',0,1,current_date,
  (select id from eg_module where name='EGF'));

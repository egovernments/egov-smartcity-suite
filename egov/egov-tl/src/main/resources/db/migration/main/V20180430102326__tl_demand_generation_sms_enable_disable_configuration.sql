
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) 
values (nextval('SEQ_EG_APPCONFIG'),'ENABLE SMS/EMAIL FOR DEMAND GENERATION','Enable/Disable Sms and Email for Manual Demand Generation', 0,
(select id from eg_user where username='system'),(select id from eg_user where username='system'),
now(),now(),(select id from eg_module where name='Trade License'));
 
 Insert into eg_appconfig_values (id, key_id, effective_from, createdby,lastmodifiedby,createddate,lastmodifieddate,value, version) values 
(nextval('SEQ_EG_APPCONFIG_VALUES'), (select id from eg_appconfig where key_name = 'ENABLE SMS/EMAIL FOR DEMAND GENERATION' and
module = (select id from eg_module where name = 'Trade License')),now(), 
(select id from eg_user where username='system'),(select id from eg_user where username='system'),
now(),now(), 'NO', 0);

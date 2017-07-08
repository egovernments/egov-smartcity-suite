Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'PROCEEDWITHOUTDONATIONAMOUNTPAID','Proceed workflow without donation amount paid',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROCEEDWITHOUTDONATIONAMOUNTPAID' and module=(select id from eg_module where name='Water Tax Management')), '01-Jan-2016', 'NO', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);


--rollback delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='PROCEEDWITHOUTDONATIONAMOUNTPAID' and module = (select id from eg_module where name='Water Tax Management'))
--rollback delete from eg_appconfig where key_name='PROCEEDWITHOUTDONATIONAMOUNTPAID' and module = (select id from eg_module where name='Water Tax Management');

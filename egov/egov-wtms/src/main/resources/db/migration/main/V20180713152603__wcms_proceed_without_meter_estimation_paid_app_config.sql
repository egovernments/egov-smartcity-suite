
DELETE from eg_appconfig_values where key_id = (select id from eg_appconfig  where key_name = 'PROCEEDWITHOUTDONATIONAMOUNTPAID' and module = (select id from eg_module where name='Water Tax Management'));

DELETE from eg_appconfig  where key_name = 'PROCEEDWITHOUTDONATIONAMOUNTPAID' and module = (select id from eg_module where name='Water Tax Management');

Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'PROCEED_WITHOUT_NONMETER_ESTIMATION_AMOUNT','Proceed workflow without meter connection estimation amount paid',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROCEED_WITHOUT_NONMETER_ESTIMATION_AMOUNT' and module=(select id from eg_module where name='Water Tax Management')), '01-Jan-2016', 'NO', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);


Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'PROCEED_WITHOUT_METER_ESTIMATION_AMOUNT','Proceed workflow without meter connection estimation amount paid',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROCEED_WITHOUT_METER_ESTIMATION_AMOUNT' and module=(select id from eg_module where name='Water Tax Management')), '01-Jan-2016', 'NO', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);







Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,
createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),
'ENABLEBILLSCHEDULAR','DemandBill Schedular is enabled or not',
0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ENABLEBILLSCHEDULAR'),
 current_date, 'YES',0);
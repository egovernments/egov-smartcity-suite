
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module)
values (nextval('SEQ_EG_APPCONFIG'),'ADDITIONALCONNECTIONALLOWEDIFPTDUE','To check whether Additional water tap connection application allowed if PT Tax due present',
0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION )
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADDITIONALCONNECTIONALLOWEDIFPTDUE'), now(), 'NO',0);

Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module)
values (nextval('SEQ_EG_APPCONFIG'),'CONNECTIONALLOWEDIFPTDUE','To check whether Water tap connection application allowed if PT Tax due present',
0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION )
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CONNECTIONALLOWEDIFPTDUE'), now(), 'YES',0);
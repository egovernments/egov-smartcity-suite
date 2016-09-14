
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,
lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'DemandReasonGlcodeMap',
'Glcodes for DemandReason',0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) 
VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DemandReasonGlcodeMap'),
 current_date,  'WTAXDONATION=1100201'||chr(10)||'WTAXSECURITY=1100201'||chr(10)||'WTAXFIELDINSPEC=1407011'||chr(10)||'WTAXCHARGES=1405016'||chr(10)||'WTADVANCE=3504106'
,0);
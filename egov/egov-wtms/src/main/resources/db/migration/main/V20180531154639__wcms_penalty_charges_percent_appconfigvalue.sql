
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'WCMS_PENALTY_CHARGES_PERCENTAGE','Wcms Percentage for Calculation of penalty charges',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='WCMS_PENALTY_CHARGES_PERCENTAGE' and module=(select id from eg_module where name='Water Tax Management')), now(), '50', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);


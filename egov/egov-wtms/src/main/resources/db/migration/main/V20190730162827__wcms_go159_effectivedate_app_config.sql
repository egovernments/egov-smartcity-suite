Insert into eg_appconfig (id,keyname,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'WCMS_ESTIMATIONNOTICE_GO159_EFFECTIVEDATE','GO 159 effective date for estimation notice generation',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));



Insert into eg_appconfig_values ( ID, CONFIG, EFFECTIVEFROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE keyname='WCMS_ESTIMATIONNOTICE_GO159_EFFECTIVEDATE' and module=(select id from eg_module where name='Water Tax Management')), now(), '09/07/2018', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);


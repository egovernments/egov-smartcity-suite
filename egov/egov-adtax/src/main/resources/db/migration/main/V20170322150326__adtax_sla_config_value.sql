INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'SLAFORNEWADVERTISEMENT','NEW ADVERTISEMENT SLA VALUE',0, (select id from eg_module where name='Advertisement Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SLAFORNEWADVERTISEMENT' AND MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,'15',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'SLAFORRENEWADVERTISEMENT','RENEW ADVERTISEMENT SLA VALUE',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SLAFORRENEWADVERTISEMENT' AND MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,'7',0);

alter table egadtax_permitdetails add column applicationtype character varying(20)  default 0;
update egadtax_permitdetails set applicationtype=1 where previousapplicationid is not null;
create sequence seq_egtl_configuration;

create table egtl_configuration(
id numeric primary key,
key varchar(50),
value varchar(100),
description varchar (200),
createdby numeric,
createddate timestamp without time zone,
lastmodifiedby numeric,
lastmodifieddate timestamp without time zone,
version bigint
);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'INCLUDE_DIGITAL_SIGN_WORKFLOW', 'false','Enable/Disable digital signature in workflow process',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'DEPARTMENT_FOR_GENERATEBILL', 'H','Department code used for generating Bill',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'NEW_APPTYPE_DEFAULT_SLA', '15',
'Default resolution time(in days) for processing New Application',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'RENEW_APPTYPE_DEFAULT_SLA', '7',
'Default resolution time(in days) for processing Renewal Application',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'CLOSURE_APPTYPE_DEFAULT_SLA', '7',
'Default resolution time(in days) for processing Closure Application',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'NOTIFY_ON_DEMAND_GENERATION', 'false','Notify citizen on demand generation',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'TL_CORPORATION_ACT', '','Trade license act for corporation',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'TL_MUNICIPALITY_ACT', '', 'Trade license act for Muncipalities',(select id from eg_user where username='system' and type='SYSTEM'),now(),(select id from eg_user where username='system' and type='SYSTEM'),now(),0);

--Delete existing appconfig
delete from eg_appconfig_values  where key_id  in (select id from eg_appconfig  where module = (select id from eg_module where name = 'Trade License'));

delete from eg_appconfig where module=(select id from eg_module where name ='Trade License');


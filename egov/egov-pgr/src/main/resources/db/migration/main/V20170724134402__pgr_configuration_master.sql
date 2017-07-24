create sequence seq_egpgr_configuration;

create table egpgr_configuration(
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

create unique index idx_egpgr_configuration_key on egpgr_configuration(key);

INSERT INTO egpgr_configuration values (nextval('seq_egpgr_configuration'), 'DEFAULT_RESOLUTION_SLA_IN_HOURS', '48',
'Default complaint resolution SLA time in hours',1,now(),1,now(),0);

INSERT INTO egpgr_configuration values (nextval('seq_egpgr_configuration'), 'DEFAULT_COMPLAINT_PRIORITY', 'M',
'Default complaint priority code (L,M,H)',1,now(),1,now(),0);

INSERT INTO egpgr_configuration values (nextval('seq_egpgr_configuration'), 'USE_AUTO_COMPLETE_FOR_COMPLAINT_TYPE', 'true',
'To use autocomplete or use Category and Complaint Type as dropdowns (true, false)',1,now(),1,now(),0);

INSERT INTO egpgr_configuration values (nextval('seq_egpgr_configuration'), 'ASSIGN_REOPENED_COMPLAINT_BASEDON_ROUTER_POSITION', 'false',
'Assign reopened complaints to position defined in router configuration (true, false)',1,now(),1,now(),0);
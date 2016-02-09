create table egtl_license_aud(
id integer,
rev integer,
revtype numeric,
license_type varchar(32),
NAME_OF_ESTAB varchar(256),
PHONENUMBER varchar(16),
STARTDATE date,
appl_date timestamp,
appl_num varchar(128),
OWNERSHIP_TYPE varchar(120),
TRADE_AREA_WEIGHT bigint,
address varchar(250),
agreement_Document_No varchar(50),
agreement_Date date,
state_id bigint,
lastModifiedDate timestamp,
lastModifiedBy bigint
);

create table egtl_trade_license_aud(
id integer,
rev integer
);

---------------START-----------------

CREATE TABLE egwtr_water_supply_type(
	id bigint NOT NULL,
	code character varying(25) NOT NULL,
	watersupplytype character varying(50) NOT NULL,
	description character varying(255) NOT NULL,
	active boolean NOT NULL,
	createddate timestamp without time zone not null,
	lastmodifieddate timestamp without time zone,
	createdby bigint NOT NULL,
	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	CONSTRAINT pk_supply_type PRIMARY KEY (id),
	CONSTRAINT unq_supply_type_code UNIQUE (code),
	CONSTRAINT unq_supply_type_watersupplytype UNIQUE (watersupplytype)
);

CREATE SEQUENCE seq_egwtr_water_supply_type;

---------------END-----------------



---------Water Supply Type Audit------

CREATE TABLE egwtr_water_supply_type_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(25),
watersupplytype character varying(100),
description character varying(255),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_water_supply_type_aud ADD CONSTRAINT pk_egwtr_water_supply_type_aud PRIMARY KEY (id, rev);

---------------END-----------------

-----------------START--------------------
Insert into egwtr_water_supply_type (id,code,watersupplytype,description,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (1,'REGULAR','Regular','Regular Supply Type','true',to_timestamp('2017-02-15 11:04:27.93748','null'),to_timestamp('2017-02-15 11:04:27.93748','null'),1,1,0);
Insert into egwtr_water_supply_type (id,code,watersupplytype,description,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (2,'SEMI BULK','Semi Bulk','Semi Bulk Supply Type','true',to_timestamp('2017-02-15 11:04:27.93748','null'),to_timestamp('2017-02-15 11:04:27.93748','null'),1,1,0);
Insert into egwtr_water_supply_type (id,code,watersupplytype,description,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (3,'BULK','Bulk','Bulk Supply Type','true',to_timestamp('2017-02-15 11:04:27.93748','null'),to_timestamp('2017-02-15 11:04:27.93748','null'),1,1,0);
------------------END--------------------- 

ALTER TABLE egwtr_connectiondetails ADD COLUMN waterSupply bigint;
ALTER TABLE egwtr_connectiondetails ADD CONSTRAINT fk_conndtl_supply FOREIGN KEY (watersupply) REFERENCES egwtr_water_supply_type(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

--rollback alter table egwtr_connectiondetails drop constraint fk_conndtl_supply;
--rollback alter table egwtr_connectiondetails drop column watersupply; 
--rollback delete from egwtr_water_supply_type where code = 'Regular' and waterSupplyType='Regular Supply Type';
--rollback delete from egwtr_water_supply_type where code = 'Semi Bulk' and waterSupplyType='Semi Bulk Supply Type';
--rollback delete from egwtr_water_supply_type where code = 'Bulk' and waterSupplyType='Bulk Supply Type';
--rollback drop table egwtr_water_supply_type_aud;
--rollback drop sequence seq_egwtr_water_supply_type;
--rolback drop table egwtr_water_supply_type;








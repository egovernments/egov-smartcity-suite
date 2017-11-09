
---------------START------------------

CREATE TABLE egwtr_metered_rates(
	id bigint NOT NULL,
	description character varying(1024),
	slabname character varying(100) NOT NULL,
	createddate timestamp without time zone NOT NULL,
	createdby bigint NOT NULL,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	constraint pk_metered_rates_id PRIMARY KEY (id),
	constraint fk_metered_rates_detail_modifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id),
	constraint fk_metered_rates_detail_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id)
);

CREATE SEQUENCE seq_egwtr_metered_rates;

---------------END-------------------

---------------START------------------

CREATE TABLE egwtr_metered_rates_detail(
	id bigint NOT NULL,
	meteredrate bigint NOT NULL,
	rateamount double precision,
	flatamount double precision,
	recursive boolean,
	recursivefactor double precision,
	recursiveamount double precision,
	fromdate date,
	todate date,
	createddate timestamp without time zone NOT NULL,
	createdby bigint NOT NULL,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	constraint pk_metered_rates_detail_id PRIMARY KEY (id),
	constraint fk_metered_rates_detail_modifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id),
	constraint fk_metered_rates_detail_meteredrate FOREIGN KEY (meteredrate) REFERENCES egwtr_metered_rates(id),
	constraint fk_metered_rates_detail_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id)
);

CREATE SEQUENCE seq_egwtr_metered_rates_detail;

---------------END-------------------

---------------START------------------
CREATE TABLE egwtr_metered_rates_aud(
	id bigint not null,
	revtype integer not null,
	rev numeric,
	slabname character varying(100),
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	CONSTRAINT pk_egwtr_metered_rates_aud PRIMARY KEY (id,rev)
);

---------------END-------------------

---------------START------------------
CREATE TABLE egwtr_metered_rates_detail_aud(
	id bigint not null,
	revtype integer not null,
	rev numeric,
	meteredrate bigint NOT NULL,
	rateamount double precision,
	flatamount double precision,
	recursive boolean NOT NULL,
	recursivefactor double precision,
	recursiveamount double precision,
	fromdate date,
	todate date,	
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	CONSTRAINT pk_egwtr_metered_rates_detail_aud PRIMARY KEY (id,rev)
);

---------------END-------------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'MeteredRatesMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Metered Rates Master', 11);

--rollback delete from eg_module where name = 'MeteredRatesMaster' and parentmodule=(select id from eg_module where name='WaterTaxMasters');
--rollback drop table egwtr_metered_rates_detail;
--rollback drop table egwtr_metered_rates;
--rollback drop table egwtr_metered_rates_detail_aud;
--rollback drop table egwtr_metered_rates_aud;
--rollback drop sequence seq_egwtr_metered_rates_detail;
--rollback drop sequence seq_egwtr_metered_rates;










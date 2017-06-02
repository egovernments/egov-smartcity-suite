
---------------START------------------

CREATE TABLE egwtr_usage_slab(
	id bigint NOT NULL,
	slabname character varying(100) NOT NULL,
	usage character varying (100) NOT NULL,
	fromvolume bigint,
	tovolume bigint,
	active boolean NOT NULL,
	createddate timestamp without time zone NOT NULL,
	createdby bigint NOT NULL,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	constraint pk_usageslab_id PRIMARY KEY (id),
	constraint unq_usageslab_slabname UNIQUE (slabname)
);

CREATE SEQUENCE seq_egwtr_usage_slab;

---------------END-------------------

---------------START------------------
CREATE TABLE egwtr_usage_slab_aud(
	id bigint not null,
	revtype integer not null,
	rev numeric,
	slabname character varying(100),
	usage character varying(100),
	fromvolume bigint,
	tovolume bigint,
	active boolean,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	CONSTRAINT pk_egwtr_usage_slab PRIMARY KEY (id,rev)
);

---------------END-------------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'UsageSlabMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Usage Slab Master', 2);

--rollback delete from eg_module where name = 'UsageSlabMaster' and parentmodule=(select id from eg_module where name='WaterTaxMasters');
--rollback drop table egwtr_usage_slab;
--rollback drop table egwtr_usage_slab_aud;
--rollback drop sequence seq_egwtr_usage_slab;









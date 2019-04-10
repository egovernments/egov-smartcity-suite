CREATE TABLE eg_third_party_bill_integration (
	id integer NOT NULL,
	tp_bill_no character varying(128),
	source character varying(128),
	bill_register_id integer,
	createdby integer,
	lastmodifiedby integer,
	lastmodifieddate timestamp without time zone,
	createddate timestamp without time zone,
	version integer DEFAULT 0
);

ALTER TABLE ONLY eg_third_party_bill_integration
	ADD CONSTRAINT eg_third_party_bill_integration_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_third_party_bill_integration
	ADD CONSTRAINT fk_eg_billgister_id FOREIGN KEY (bill_register_id) REFERENCES eg_billregister(id);

create sequence seq_eg_tpbillintegration start 1;

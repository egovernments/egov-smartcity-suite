CREATE TABLE egadtax_demandgenerationlog (
	id bigint NOT NULL,
	installmentyear character varying(32),
	executionstatus character varying(32),
	demandGenerationStatus character varying(32),
	createddate timestamp without time zone,
	createdby bigint,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	version bigint
);

ALTER TABLE ONLY egadtax_demandgenerationlog
	ADD CONSTRAINT pk_egadtax_demandgenerationlog PRIMARY KEY (id);

CREATE TABLE egadtax_demandgenerationlogdetail (
	id bigint NOT NULL,
	demandgenerationlog bigint NOT NULL, 
	advertisement bigint NOT NULL,
	status character varying(32),
	detail character varying(200),
	version bigint
);

ALTER TABLE ONLY egadtax_demandgenerationlogdetail
	ADD CONSTRAINT pk_egadtax_demandgenerationlogdetail PRIMARY KEY (id);
	
ALTER TABLE ONLY egadtax_demandgenerationlogdetail
	ADD CONSTRAINT fk_egadtax_demandgenerationlogdetail FOREIGN KEY (demandgenerationlog) REFERENCES egadtax_demandgenerationlog(id);

ALTER TABLE ONLY egadtax_demandgenerationlogdetail
	ADD CONSTRAINT fk_egadtax_advertisement FOREIGN KEY (advertisement) REFERENCES egadtax_advertisement(id);

CREATE SEQUENCE seq_egadtax_demandgenerationlog;
CREATE SEQUENCE seq_egadtax_demandgenerationlogdetail;

--rollback drop table egadtax_demandgenerationlogdetail;
--rollback drop table egadtax_demandgenerationlog;
--rollback drop sequence seq_egadtax_demandgenerationlog;
--rollback drop sequence seq_egadtax_demandgenerationlogdetail;

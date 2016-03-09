CREATE TABLE egtl_demandgeneration (
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

ALTER TABLE ONLY egtl_demandgeneration
    ADD CONSTRAINT pk_egtl_demandgeneration PRIMARY KEY (id);

CREATE TABLE egtl_demandgenerationdetail (
    id bigint,
    licenseDemandGeneration bigint,
    tradelicense bigint,
    status character varying(32),
    detail character varying(256),
    version bigint
);

ALTER TABLE ONLY egtl_demandgenerationdetail
    ADD CONSTRAINT pk_egtl_demandgenerationdetail PRIMARY KEY (id);

ALTER TABLE ONLY egtl_demandgenerationdetail
    ADD CONSTRAINT fk_egtl_demandgeneration FOREIGN KEY (licenseDemandGeneration) REFERENCES egtl_demandgeneration(id);

ALTER TABLE ONLY egtl_demandgenerationdetail
    ADD CONSTRAINT fk_egtl_tradelicense FOREIGN KEY (tradelicense) REFERENCES egtl_license(id);

CREATE SEQUENCE seq_egtl_demandgeneration;
CREATE SEQUENCE seq_egtl_demandgenerationdetail;

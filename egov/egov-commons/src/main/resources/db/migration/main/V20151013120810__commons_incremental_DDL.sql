------------------START------------------
CREATE TABLE egw_contractor_grade (
    id bigint NOT NULL,
    grade character varying(20) NOT NULL,
    description character varying(100) NOT NULL,
    min_amount double precision NOT NULL,
    max_amount double precision NOT NULL,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    CONSTRAINT pk_contractor_grade PRIMARY KEY (id),
    CONSTRAINT unq_contractorgrade_grade UNIQUE (grade),
    CONSTRAINT sys_c0010480 CHECK ((min_amount >= (0)::double precision)),
    CONSTRAINT sys_c0010481 CHECK ((max_amount >= (0)::double precision))
);

CREATE SEQUENCE seq_egw_contractor_grade;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_typeofwork (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    parentid bigint,
    description character varying(1000) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    partytypeid bigint
);
CREATE SEQUENCE seq_egw_typeofwork;
    
ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT pk_egw_typeofwork PRIMARY KEY (id);
    
ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT fk_typeofwork_typeofwork FOREIGN KEY (parentid) REFERENCES egw_typeofwork(id);

ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT fk_typeofwork_partytype FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);
    
CREATE INDEX idx_egw_typeofwork_parent ON egw_typeofwork USING btree (parentid);
CREATE INDEX idx_egw_typeofwork_partytype ON egw_typeofwork USING btree (partytypeid);
-------------------END-------------------

--rollback DROP SEQUENCE seq_egw_typeofwork;
--rollback DROP TABLE egw_typeofwork;

--rollback DROP SEQUENCE seq_egw_contractor_grade;
--rollback DROP TABLE egw_contractor_grade;
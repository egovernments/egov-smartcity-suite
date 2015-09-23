CREATE TABLE egtl_mstr_UnitOfMeasure(
  id bigint NOT NULL,
  code character varying(50) not null, 
  name character varying(50) not null, 
  active boolean NOT NULL  DEFAULT TRUE,
  createdby bigint NOT NULL ,
  lastmodifiedby bigint NOT NULL, 
  createddate timestamp without time zone NOT NULL default CURRENT_DATE,
  lastmodifieddate timestamp without time zone NOT NULL,
  version bigint,
  CONSTRAINT pk_egtl_mstr_UnitOfMeasure PRIMARY KEY (id),
  CONSTRAINT fk_egtl_mstr_UnitOfMeasure_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
  CONSTRAINT fk_egtl_mstr_UnitOfMeasure_modifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id),
  CONSTRAINT unq_tluom_name UNIQUE (name),
  CONSTRAINT unq_tluom_code UNIQUE (code)
);


CREATE SEQUENCE SEQ_egtl_mstr_UnitOfMeasure;

--rollback drop sequence SEQ_egtl_mstr_UnitOfMeasure;
--rollback DROP TABLE egtl_mstr_UnitOfMeasure;

CREATE SEQUENCE seq_pgr_complaintstatus;

CREATE TABLE pgr_complaintstatus
(
  id bigint NOT NULL,
  name character varying(25),
CONSTRAINT pk_complaintstatus PRIMARY KEY (id)     
);

CREATE SEQUENCE seq_pgr_complaintstatus_mapping;

CREATE TABLE pgr_complaintstatus_mapping (
 id bigint NOT NULL,
 role_id bigint NOT NULL,
 complaintstatus_id NOT NULL,
CONSTRAINT pk_complaintstatus_mapping PRIMARY KEY (id)
);

alter table pgr_complaint drop column status; ;

alter table pgr_complaint add column status bigint;

alter table pgr_complaint add  constraint fk_complaint_status foreign key (status)  references pgr_complaintstatus(id);

alter table pgr_complaintstatus_mappin  add column orderno bigint;

--rollback drop table pgr_complaintstatus_mapping;
--rollback drop seq_pgr_complaintstatus_mapping;
--rollback drop table pgr_complaintstatus;
--rollback drop sequnce seq_pgr_complaintstatus;
--rollback alter table pgr_complaint drop column status;
--rollback alter table pgr_complaint add column status character varying(25);


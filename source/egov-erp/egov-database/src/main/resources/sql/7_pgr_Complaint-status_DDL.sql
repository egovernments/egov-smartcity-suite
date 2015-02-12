CREATE SEQUENCE seq_pgr_complaintstatus;

CREATE TABLE pgr_complaintstatus
(
  id bigint NOT NULL,
  name character varying(25),
CONSTRAINT pk_complaintstatus PRIMARY KEY (id)     
);

CREATE SEQUENCE seq_pgr_complaintstatus_mapping

CREATE TABLE pgr_complaintstatus_mapping (
 id bigint NOT NULL,
 role_id bigint NOT NULL,
 complaintstatus_id NOT NULL,
CONSTRAINT pk_complaintstatus_mapping PRIMARY KEY (id)
)

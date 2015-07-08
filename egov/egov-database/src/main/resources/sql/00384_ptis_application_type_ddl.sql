create table egpt_application_type (
	id bigint not null,
	code character varying(25) not null,
	name character varying(50) not null,
	resolutiontime bigint not null,
	description character varying(50),
	createddate timestamp without time zone not null,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint not null,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_egpt_application_type PRIMARY KEY (id),
	CONSTRAINT unq_egpt_application_code UNIQUE (code),
	CONSTRAINT unq_egpt_application_name UNIQUE (name),
	CONSTRAINT fk_egpt_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_egpt_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egpt_application_type;

alter table egpt_document_type add id_application_type bigint references egpt_application_type(id);

--rollback alter table egpt_document_type drop id_application_type;
--rollback drop SEQUENCE seq_egpt_application_type;
--rollback drop table egpt_application_type;
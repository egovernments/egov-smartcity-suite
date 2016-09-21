CREATE TABLE egswtax_applicationdetails_documents
(
  id bigint NOT NULL,
  filestoreid bigint NOT NULL,
  applicationdetails bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL
);

ALTER TABLE ONLY egswtax_applicationdetails_documents
    ADD CONSTRAINT pk_appDtls_documents_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_applicationdetails_documents
    ADD CONSTRAINT fk_appDtls_id_fkey FOREIGN KEY (applicationdetails) REFERENCES egswtax_applicationdetails (id);

CREATE SEQUENCE seq_egswtax_applicationdetails_documents;   

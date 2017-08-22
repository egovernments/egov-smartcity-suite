
CREATE TABLE EGF_DOCUMENTS
(
  id bigint NOT NULL,
  objectid bigint NOT NULL,
  objecttype character varying(128) NOT NULL,
  filestoreid bigint NOT NULL
);

ALTER TABLE egf_documents ADD CONSTRAINT pk_document PRIMARY KEY (id);
ALTER TABLE egf_documents ADD CONSTRAINT fk_objectid FOREIGN KEY (objectid) REFERENCES eg_billregister (id);

CREATE SEQUENCE SEQ_EGF_DOCUMENTS;

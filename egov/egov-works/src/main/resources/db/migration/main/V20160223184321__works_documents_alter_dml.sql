ALTER TABLE egw_documents ADD COLUMN id bigint NOT NULL;
ALTER TABLE egw_documents ADD CONSTRAINT pk_documents PRIMARY KEY (id);
ALTER TABLE egw_documents ADD CONSTRAINT fk_objectid FOREIGN KEY (objectid) REFERENCES egw_lineestimate (id);
CREATE SEQUENCE SEQ_EGW_DOCUMENTS;

--rollback DROP SEQUENCE SEQ_EGW_DOCUMENTS;


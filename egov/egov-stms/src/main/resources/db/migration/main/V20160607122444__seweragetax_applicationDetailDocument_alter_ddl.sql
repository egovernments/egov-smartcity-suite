
 ALTER TABLE egswtax_applicationdetails_documents ADD COLUMN documenttypemaster bigint NOT NULL;
 ALTER TABLE egswtax_applicationdetails_documents ADD COLUMN documentnumber character varying(50) ;
 ALTER TABLE egswtax_applicationdetails_documents ADD COLUMN documentdate date ;

 ALTER TABLE ONLY egswtax_applicationdetails_documents
    ADD CONSTRAINT fk_doctypemaster_id_fkey FOREIGN KEY (documenttypemaster) REFERENCES egswtax_document_type_master (id);

CREATE TABLE egswtax_documents
(
  filestoreid bigint NOT NULL,
  applicationDetailDocument bigint NOT NULL
);

 ALTER TABLE ONLY egswtax_applicationdetails_documents DROP COLUMN filestoreid;

drop table eglc_legalcase_document;

CREATE TABLE eglc_legalcase_document
(
id bigint NOT NULL,
documentname character varying(100) NOT NULL,
legalcase bigint NOT NULL,--Foreign Key of  eglc_legalcase
version numeric NOT NULL,
  CONSTRAINT pk_legalcase_docs PRIMARY KEY (id),
CONSTRAINT fk_legalcassedocs_legalcaseid FOREIGN KEY (legalcase)
      REFERENCES eglc_legalcase (id)
);

create table eglc_documents
(
 legalcaseDocId bigint NOT NULL, -- Foreign Key of  eglc_legalcase_document
  filestoreid bigint NOT NULL, -- Foreign Key of  eg_filestoremap
  CONSTRAINT fk_legalcasedocument_filestoreid FOREIGN KEY (filestoreid)
      REFERENCES eg_filestoremap (id) ,
  CONSTRAINT fk_legaldocument_id FOREIGN KEY (legalcaseDocId)
      REFERENCES eglc_legalcase_document (id) 
);
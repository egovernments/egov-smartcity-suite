CREATE TABLE egbpa_document
(
  filestoreid bigint NOT NULL,
  applicationdocumentid bigint NOT NULL,
  CONSTRAINT fk_egbpa_document_appdocument FOREIGN KEY(applicationdocumentid)
         REFERENCES egbpa_application_document(ID)
);
CREATE INDEX INDX_egbpa_documents_filestoreid ON egbpa_document (filestoreid);
CREATE INDEX INDX_egbpa_documents_applicationdocumentsid ON egbpa_document (applicationdocumentid);

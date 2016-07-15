DROP table eglc_judgment_document;

CREATE TABLE eglc_judgment_document
(
id bigint NOT NULL,
documentname character varying(100) NOT NULL,
judgment bigint NOT NULL,
version numeric NOT NULL,
  CONSTRAINT pk_judgment_document PRIMARY KEY (id),
CONSTRAINT fk_judgmentdocs_judgmentid FOREIGN KEY (judgment)
      REFERENCES eglc_judgment (id)
);
CREATE INDEX idx_judgmentdocument_documentname ON eglc_judgment_document (documentname);
CREATE INDEX idx_judgmentdocument_judgment ON eglc_judgment_document (judgment);

COMMENT ON TABLE eglc_judgment_document IS 'Judgment Document tables';
COMMENT ON COLUMN eglc_judgment_document.documentname IS 'Document Names';
COMMENT ON COLUMN eglc_judgment_document.judgment IS 'Foreign Key of eglc_judgment';

create table eglc_judgment_filestore
(
 judgmentDocId bigint NOT NULL, 
  filestoreid bigint NOT NULL, 
  CONSTRAINT fk_judgmentdocument_filestoreid FOREIGN KEY (filestoreid)
      REFERENCES eg_filestoremap (id) ,
  CONSTRAINT fk_judgmentdocument_id FOREIGN KEY (judgmentDocId)
      REFERENCES eglc_judgment_document (id) 
);
CREATE INDEX idx_judgmentdocid_judgmentDocId ON eglc_judgment_filestore (judgmentDocId);
CREATE INDEX idx_judgmentdocid_filestoreid ON eglc_judgment_filestore (filestoreid);

COMMENT ON TABLE eglc_judgment_filestore IS 'Storing Judgment Document id tables';
COMMENT ON COLUMN eglc_judgment_filestore.judgmentDocId IS 'Judgment Document Id';
COMMENT ON COLUMN eglc_judgment_filestore.filestoreid IS 'Foreign Key of eg_filestoremap';

------ALTER modify to double
 ALTER TABLE eglc_judgment ALTER COLUMN costawarded TYPE double precision;
 ALTER TABLE eglc_judgment ALTER COLUMN compensationawarded TYPE double precision;

------Inert into eg_action ----------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-Judgment','/judgment/new/',(select id from eg_module where name='LCMS Transactions' ),1,
'Create Judgment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='New-Judgment'));

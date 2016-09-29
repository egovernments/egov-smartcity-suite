ALTER TABLE eglc_appeal DROP COLUMN status;
--- boolean to charcter 
ALTER TABLE eglc_judgmentimpl  ALTER COLUMN iscompiled TYPE character varying(20);

---Insert into egw_status
Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Judgment Implemented',now(),'Judgment_Implemented',1);

--Insert into eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-JudgmentImpl','/judgmentimpl/new',(select id from eg_module where name='LCMS Transactions' ),1,
'New-JudgmentImpl',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='New-JudgmentImpl'));

---create table for appeal_documents
CREATE TABLE eglc_appeal_documents
(
id bigint NOT NULL,
documentname character varying(100) NOT NULL,
appeal bigint NOT NULL,
version numeric NOT NULL,
  CONSTRAINT pk_appeal_document PRIMARY KEY (id),
CONSTRAINT fk_appealdocs_appealid FOREIGN KEY (appeal)
      REFERENCES eglc_appeal (id)
);
CREATE SEQUENCE seq_eglc_appeal_document;
CREATE INDEX idx_appeal_documents_documentname ON eglc_appeal_documents (documentname);
CREATE INDEX idx_appeal_documents_appeal ON eglc_appeal_documents (appeal);

COMMENT ON TABLE eglc_appeal_documents IS 'Appeal Document tables';
COMMENT ON COLUMN eglc_appeal_documents.documentname IS 'Document Names';
COMMENT ON COLUMN eglc_appeal_documents.appeal IS 'Foreign Key of eglc_appeal';

create table eglc_appeal_filestore
(
 appealDocId bigint NOT NULL, 
  filestoreid bigint NOT NULL, 
  CONSTRAINT fk_appealdocument_filestoreid FOREIGN KEY (filestoreid)
      REFERENCES eg_filestoremap (id) ,
  CONSTRAINT fk_appealdocument_id FOREIGN KEY (appealDocId)
      REFERENCES eglc_appeal_documents (id) 
);
CREATE INDEX idx_appealdocid_appealDocId ON eglc_appeal_filestore (appealDocId);
CREATE INDEX idx_appealdocid_filestoreid ON eglc_appeal_filestore (filestoreid);

COMMENT ON TABLE eglc_appeal_filestore IS 'Storing Appeal Document id tables';
COMMENT ON COLUMN eglc_appeal_filestore.appealDocId IS 'Appeal Document Id';
COMMENT ON COLUMN eglc_appeal_filestore.filestoreid IS 'Foreign Key of eg_filestoremap';

ALTER TABLE eglc_judgmentimpl  ALTER COLUMN iscompiled DROP not null;
ALTER TABLE eglc_contempt  ALTER COLUMN iscommapprrequired DROP not null;
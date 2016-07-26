DROP table eglc_lcio_document ;

CREATE TABLE eglc_lcio_document 
(
id bigint NOT NULL,
documentname character varying(100) NOT NULL,
lcinterimorder bigint NOT NULL,
version numeric NOT NULL,
  CONSTRAINT pk_lcio_document PRIMARY KEY (id),
CONSTRAINT fk_lciodocs_lcioid FOREIGN KEY (lcinterimorder)
      REFERENCES eglc_lcinterimorder (id)
);
CREATE INDEX idx_lcinterimorderlcInterimOrderdocument_documentname ON eglc_lcio_document (documentname);
CREATE INDEX idx_lcinterimorderdocument_lcinterimorder ON eglc_lcio_document (lcinterimorder);

COMMENT ON TABLE eglc_lcio_document IS 'LcInterimOrder Document tables';
COMMENT ON COLUMN eglc_lcio_document.documentname IS 'Document Names';
COMMENT ON COLUMN eglc_lcio_document.lcinterimorder IS 'Foreign Key of eglc_lcinterimorder';

create table eglc_lcinterimorder_filestore
(
 lcinterimorderDocId bigint NOT NULL, 
  filestoreid bigint NOT NULL, 
  CONSTRAINT fk_lcinterimorderdocument_filestoreid FOREIGN KEY (filestoreid)
      REFERENCES eg_filestoremap (id) ,
  CONSTRAINT fk_lcinterimorderdocument_id FOREIGN KEY (lcinterimorderDocId)
      REFERENCES eglc_lcio_document (id) 
);
CREATE INDEX idx_lcinterimorderdocid_lcinterimorderDocId ON eglc_lcinterimorder_filestore (lcinterimorderDocId);
CREATE INDEX idx_lcinterimorderdocid_filestoreid ON eglc_lcinterimorder_filestore (filestoreid);

COMMENT ON TABLE eglc_lcinterimorder_filestore IS 'Storing LcInteriomOrder Document id tables';
COMMENT ON COLUMN eglc_lcinterimorder_filestore.lcinterimorderDocId IS 'LcInteriomOrder Document Id';
COMMENT ON COLUMN eglc_lcinterimorder_filestore.filestoreid IS 'Foreign Key of eg_filestoremap';

---Insert Into eg_action 
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-LcInterimOrder','/lcinterimorder/new/',(select id from eg_module where name='LCMS Transactions' ),1,
'Create LcInterimOrder',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='New-LcInterimOrder'));

---Alter table columns
ALTER TABLE eglc_lcinterimorder RENAME COLUMN intordertypeid TO interimorder;

ALTER TABLE eglc_lcinterimorder RENAME COLUMN revdfromhod TO reportfromhod;

ALTER TABLE eglc_lcinterimorder DROP COLUMN status;

---updating status description
delete from egw_status where moduletype='Legal Case' and code='Hearing';

update egw_status set description='In Progress' ,code='IN_PROGRESS' where description='Created Hearing';

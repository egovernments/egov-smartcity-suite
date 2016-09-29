alter table EGLC_PWR add column  pwrapprovaldate date;


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'AddCounterAffidavit','/counterAffidavit/create/',(select id from eg_module 
 where name='LCMS Transactions'),1,'AddCounterAffidavit',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='AddCounterAffidavit'));


drop table eglc_pwr_document;

CREATE TABLE eglc_pwr_document
(
id bigint NOT NULL,
documentname character varying(100),
pwrId bigint NOT NULL,--Foreign Key of  eglc_legalcase
  CONSTRAINT pk_pwr_docs PRIMARY KEY (id),
CONSTRAINT fk_pwrdocs_legalcasepwrid FOREIGN KEY (pwrId)
      REFERENCES EGLC_PWR (id)
);

create table eglc_pwr_filestore
(
 pwrDocId bigint NOT NULL, -- Foreign Key of  eglc_legalcase_document
  filestoreid bigint NOT NULL, -- Foreign Key of  eg_filestoremap
  CONSTRAINT fk_pwrdocument_filestoreid FOREIGN KEY (filestoreid)
      REFERENCES eg_filestoremap (id) ,
  CONSTRAINT fk_pwrlegaldocument_id FOREIGN KEY (pwrDocId)
      REFERENCES eglc_pwr_document (id) 
);


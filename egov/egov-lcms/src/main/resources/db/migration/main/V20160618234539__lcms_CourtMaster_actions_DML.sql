------Auditing Table------
CREATE TABLE eglc_court_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
name character varying(100),
courttype bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);
ALTER TABLE ONLY eglc_court_master_aud ADD CONSTRAINT pk_eglc_court_master_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_court_master_aud IS 'Court master auditing table';
COMMENT ON COLUMN eglc_court_master_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_court_master_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_court_master_aud.name IS 'Name of Court ';
COMMENT ON COLUMN eglc_court_master_aud.courttype IS 'Court type';
COMMENT ON COLUMN eglc_court_master_aud.active IS 'Active or Inactive';
COMMENT ON COLUMN eglc_court_master_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_court_master_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_court_master_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';

------Court master module---

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'CourtMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Court Master', 4);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CourtMaster','/courtmaster/new',(select id from eg_module where name='CourtMaster'),1,'Create Court',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CourtMaster','/courtmaster/create',(select id from eg_module where name='CourtMaster'),1,'Create-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-CourtMaster','/courtmaster/update',(select id from eg_module where name='CourtMaster'),1,'Update-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CourtMaster','/courtmaster/view',(select id from eg_module where name='CourtMaster'),1,'View-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CourtMaster','/courtmaster/edit',(select id from eg_module where name='CourtMaster'),1,'Edit-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CourtMaster','/courtmaster/result',(select id from eg_module where name='CourtMaster'),1,'Result-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CourtMaster','/courtmaster/search/view',(select id from eg_module where name='CourtMaster' ),2,' View Court',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CourtMaster','/courtmaster/search/edit',(select id from eg_module where name='CourtMaster'),3,'Modify Court',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CourtMaster','/courtmaster/ajaxsearch/view',(select id from eg_module where name='CourtMaster'),1,'Search and View Result-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CourtMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CourtMaster','/courtmaster/ajaxsearch/edit',(select id from eg_module where name='CourtMaster'),1,'Search and Edit Result-CourtMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CourtMaster'));

--------Comment on PetitionType table----
COMMENT ON TABLE eglc_petitiontype_master IS 'Petition Type master table';


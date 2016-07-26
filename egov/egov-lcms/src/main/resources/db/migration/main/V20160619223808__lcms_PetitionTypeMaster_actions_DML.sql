------Auditing Table------
CREATE TABLE eglc_petitiontype_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
petitiontype character varying(100),
courttype bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);
ALTER TABLE ONLY eglc_petitiontype_master_aud ADD CONSTRAINT pk_eglc_petitiontype_master_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_petitiontype_master_aud IS 'Petition Type master auditing table';
COMMENT ON COLUMN eglc_petitiontype_master_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_petitiontype_master_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_petitiontype_master_aud.code IS 'Code of Petition Type ';
COMMENT ON COLUMN eglc_petitiontype_master_aud.petitiontype IS 'Name of Petition Type';
COMMENT ON COLUMN eglc_petitiontype_master_aud.courttype IS 'Court type';
COMMENT ON COLUMN eglc_petitiontype_master_aud.active IS 'ISActive?';
COMMENT ON COLUMN eglc_petitiontype_master_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_petitiontype_master_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_petitiontype_master_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';

------Petition Type master module---

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'PetitionTypeMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Petition Type Master', 5);

-------Inserting eg_action ------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-PetitionTypeMaster','/petitiontypemaster/new',(select id from eg_module where name='PetitionTypeMaster'),1,'Create Petition Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-PetitionTypeMaster','/petitiontypemaster/create',(select id from eg_module where name='PetitionTypeMaster'),1,'Create-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-PetitionTypeMaster','/petitiontypemaster/update',(select id from eg_module where name='PetitionTypeMaster'),1,'Update-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-PetitionTypeMaster','/petitiontypemaster/view',(select id from eg_module where name='PetitionTypeMaster' ),1,'View-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-PetitionTypeMaster','/petitiontypemaster/edit',(select id from eg_module where name='PetitionTypeMaster' ),1,'Edit-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-PetitionTypeMaster','/petitiontypemaster/result',(select id from eg_module where name='PetitionTypeMaster'),1,'Result-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-PetitionTypeMaster','/petitiontypemaster/search/view',(select id from eg_module where name='PetitionTypeMaster'),2,'View Petition Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-PetitionTypeMaster','/petitiontypemaster/search/edit',(select id from eg_module where name='PetitionTypeMaster'  ),3,'Modify Petition Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-PetitionTypeMaster','/petitiontypemaster/ajaxsearch/view',(select id from eg_module where name='PetitionTypeMaster'),1,'Search and View Result-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-PetitionTypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-PetitionTypeMaster','/petitiontypemaster/ajaxsearch/edit',(select id from eg_module where name='PetitionTypeMaster'),1,'Search and Edit Result-PetitionTypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-PetitionTypeMaster'));


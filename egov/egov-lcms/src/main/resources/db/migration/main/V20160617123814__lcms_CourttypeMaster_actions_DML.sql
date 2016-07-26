------Auditing Table------
CREATE TABLE eglc_courttype_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
courtType character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);
ALTER TABLE ONLY eglc_courttype_master_aud ADD CONSTRAINT pk_eglc_courttype_master_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_courttype_master_aud IS 'CourtType master auditing table';
COMMENT ON COLUMN eglc_courttype_master_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_courttype_master_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_courttype_master_aud.code IS 'Code of Court Type';
COMMENT ON COLUMN eglc_courttype_master_aud.courtType IS 'Court type';
COMMENT ON COLUMN eglc_courttype_master_aud.active IS 'ISActive?';
COMMENT ON COLUMN eglc_courttype_master_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_courttype_master_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_courttype_master_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';

------Courttype module---

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'CourtTypeMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Court Type Master', 2);

----Inserting to eg_action----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CourttypeMaster','/courttypemaster/new',(select id from eg_module where name='CourtTypeMaster'),1,'Create Court Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CourttypeMaster','/courttypemaster/create',(select id from eg_module where name='CourtTypeMaster'),1,'Create-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-CourttypeMaster','/courttypemaster/update',(select id from eg_module where name='CourtTypeMaster'),1,'Update-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CourttypeMaster','/courttypemaster/view',(select id from eg_module where name='CourtTypeMaster'),1,'View-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CourttypeMaster','/courttypemaster/edit',(select id from eg_module where name='CourtTypeMaster'),1,'Edit-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CourttypeMaster','/courttypemaster/result',(select id from eg_module where name='CourtTypeMaster'),1,'Result-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CourttypeMaster','/courttypemaster/search/view',(select id from eg_module where name='CourtTypeMaster'),2,' View Court Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CourttypeMaster','/courttypemaster/search/edit',(select id from eg_module where name='CourtTypeMaster'),3,'Modify Court Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CourttypeMaster','/courttypemaster/ajaxsearch/view',(select id from eg_module where name='CourtTypeMaster'),1,'Search and View Result-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CourttypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CourttypeMaster','/courttypemaster/ajaxsearch/edit',(select id from eg_module where name='CourtTypeMaster'),1,'Search and Edit Result-CourttypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CourttypeMaster'));


--------------------End---------------------
---------InteriomType master auditing table descriptions-----
COMMENT ON TABLE eglc_interimtype_master_aud  IS 'InterimType master auditing table';
COMMENT ON COLUMN eglc_interimtype_master_aud .id IS 'Primary Key';
COMMENT ON COLUMN eglc_interimtype_master_aud .rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_interimtype_master_aud .code IS 'Code of InterimType';
COMMENT ON COLUMN eglc_interimtype_master_aud .interimordertype IS 'Interim Order Type';
COMMENT ON COLUMN eglc_interimtype_master_aud .active IS 'ISActive?';
COMMENT ON COLUMN eglc_interimtype_master_aud .lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_interimtype_master_aud .lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_interimtype_master_aud .revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';
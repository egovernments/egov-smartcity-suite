------Auditing Table------
CREATE TABLE eglc_judgmenttype_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(50),
judgmentType character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);
ALTER TABLE ONLY eglc_judgmenttype_master_aud ADD CONSTRAINT pk_eglc_judgmenttype_master_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_judgmenttype_master_aud IS 'Judgement Type master auditing table';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.code IS 'Code of Judgement Type';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.judgmentType IS 'Judgement type';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.active IS 'ISActive?';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgmenttype_master_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';
-------Inserting eg_module-----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'JudgementtypeMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Judgement Type Master', 6);
----Inserting to eg_action----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-JudgmentType','/judgmenttype/new',(select id from eg_module where name='JudgementtypeMaster'),1,'Create Judgment Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-JudgmentType','/judgmenttype/create',(select id from eg_module where name='JudgementtypeMaster'),1,'Create-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-JudgmentType','/judgmenttype/update',(select id from eg_module where name='JudgementtypeMaster' ),1,'Update-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-JudgmentType','/judgmenttype/view',(select id from eg_module where name='JudgementtypeMaster' ),1,'View-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-JudgmentType','/judgmenttype/edit',(select id from eg_module where name='JudgementtypeMaster' ),1,'Edit-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-JudgmentType','/judgmenttype/result',(select id from eg_module where name='JudgementtypeMaster' ),1,'Result-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-JudgmentType','/judgmenttype/search/view',(select id from eg_module where name='JudgementtypeMaster' ),2,'View Judgment Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-JudgmentType','/judgmenttype/search/edit',(select id from eg_module where name='JudgementtypeMaster' ),3,'Modify Judgment Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-JudgmentType','/judgmenttype/ajaxsearch/view',(select id from eg_module where name='JudgementtypeMaster' ),1,'Search and View Result-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-JudgmentType'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-JudgmentType','/judgmenttype/ajaxsearch/edit',(select id from eg_module where name='JudgementtypeMaster' ),1,'Search and Edit Result-JudgmentType',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-JudgmentType'));


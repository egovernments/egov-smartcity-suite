------Auditing Table------
CREATE TABLE eglc_governmentdepartment_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(50),
name character varying(256),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);
ALTER TABLE ONLY eglc_governmentdepartment_aud ADD CONSTRAINT pk_eglc_governmentdepartment_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_governmentdepartment_aud IS 'Government Department master auditing table';
COMMENT ON COLUMN eglc_governmentdepartment_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_governmentdepartment_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_governmentdepartment_aud.code IS 'Code of Government Department';
COMMENT ON COLUMN eglc_governmentdepartment_aud.name IS 'Government Department name';
COMMENT ON COLUMN eglc_governmentdepartment_aud.active IS 'Active or InActive';
COMMENT ON COLUMN eglc_governmentdepartment_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_governmentdepartment_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_governmentdepartment_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';


-------Inserting eg_module-----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'GovernmentDepartment', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Government Department Master', 7);


----Inserting to eg_action----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-GovernmentDepartment','/governmentdepartment/new',(select id from eg_module where name='GovernmentDepartment' ),1,'Create Government Department',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-GovernmentDepartment','/governmentdepartment/create',(select id from eg_module where name='GovernmentDepartment' ),1,'Create-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-GovernmentDepartment','/governmentdepartment/update',(select id from eg_module where name='GovernmentDepartment' ),1,'Update-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-GovernmentDepartment','/governmentdepartment/view',(select id from eg_module where name='GovernmentDepartment' ),1,'View-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-GovernmentDepartment','/governmentdepartment/edit',(select id from eg_module where name='GovernmentDepartment' ),1,'Edit-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-GovernmentDepartment','/governmentdepartment/result',(select id from eg_module where name='GovernmentDepartment' ),1,'Result-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-GovernmentDepartment','/governmentdepartment/search/view',(select id from eg_module where name='GovernmentDepartment' ),2,'View Government Department',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-GovernmentDepartment','/governmentdepartment/search/edit',(select id from eg_module where name='GovernmentDepartment' ),3,'Modify Government Department',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-GovernmentDepartment','/governmentdepartment/ajaxsearch/view',(select id from eg_module where name='GovernmentDepartment' ),1,'Search and View Result-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-GovernmentDepartment'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-GovernmentDepartment','/governmentdepartment/ajaxsearch/edit',(select id from eg_module where name='GovernmentDepartment' ),1,'Search and Edit Result-GovernmentDepartment',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-GovernmentDepartment'));


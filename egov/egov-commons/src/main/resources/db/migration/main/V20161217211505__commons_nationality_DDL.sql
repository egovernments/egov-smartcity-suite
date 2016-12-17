CREATE TABLE eg_nationality
(
  id bigint NOT NULL,
  name character varying(30) NOT NULL,
  description character varying(100) NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_eg_nationality PRIMARY KEY (id)
);

create sequence SEQ_EG_NATIONALITY;

INSERT into eg_module values (nextval('SEQ_EG_ACTION'),'CommonNationality',false,'common',(select id from eg_module where name='Common-Masters' and contextroot='common'),'Nationality',3);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'New-Nationality','/nationality/new',(select id from eg_module where name='CommonNationality' ),1,'Create Nationality',true,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Nationality','/nationality/create',(select id from eg_module where name='CommonNationality'),1,'Create-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Nationality','/nationality/view',(select id from eg_module where name='CommonNationality'),1,'View-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Nationality','/nationality/result',(select id from eg_module where name='CommonNationality'),1,'Result-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Nationality','/nationality/search/view',(select id from eg_module where name='CommonNationality'),2,'View Nationality',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Nationality','/nationality/ajaxsearch/view',(select id from eg_module where name='CommonNationality'),1,'Search and View Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Nationality','/nationality/ajaxsearch/edit',(select id from eg_module where name='CommonNationality'),1,'Search and Edit Result-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Nationality','/nationality/search/edit',(select id from eg_module where name='CommonNationality'),3,'Modify Nationality',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Nationality','/nationality/edit',(select id from eg_module where name='CommonNationality'),1,'Edit-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Nationality'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Nationality','/nationality/update',(select id from eg_module where name='CommonNationality'),1,'Update-Nationality',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Nationality'));
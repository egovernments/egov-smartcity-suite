----Auditing table
CREATE TABLE eglc_interimtype_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
interimordertype character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY eglc_interimtype_master_aud ADD CONSTRAINT pk_eglc_interimtype_master_aud PRIMARY KEY (id, rev);
-------Inserting eg_module-----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'InterimOrderMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Interim Order Master', 1);
--------Inserting to eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval
('SEQ_EG_ACTION'),'New-InterimOrder','/interimorder/new',
(select id from eg_module where name='InterimOrderMaster'),1,'Create Interim Order',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='New-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Create-InterimOrder','/interimorder/create',
(select id from eg_module where name='InterimOrderMaster'),1,'Create-InterimOrder',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Update-InterimOrder','/interimorder/update',
(select id from eg_module where name='InterimOrderMaster'),1,'Update-InterimOrder',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Update-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'View-InterimOrder','/interimorder/view',(select id from eg_module where name='InterimOrderMaster'),1,'View-InterimOrder',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-InterimOrder','/interimorder/edit',(select id from eg_module where name='InterimOrderMaster'),1,'Edit-InterimOrder',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Result-InterimOrder','/interimorder/result',(select id from eg_module where name='InterimOrderMaster' ),1,'Result-InterimOrder',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and View-InterimOrder','/interimorder/search/view',(
select id from eg_module where name='InterimOrderMaster'),2,'View Interim Order',true,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and Edit-InterimOrder','/interimorder/search/edit',
(select id from eg_module where name='InterimOrderMaster'
),3,'Modify Interim Order',true,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and View Result-InterimOrder','/interimorder/ajaxsearch/view',
(select id from eg_module where name='InterimOrderMaster'),1,
'Search and View Result-InterimOrder',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-InterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-InterimOrder','/interimorder/ajaxsearch/edit',
(select id from eg_module where name='InterimOrderMaster' ),1,'Search and Edit Result-InterimOrder',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-InterimOrder'));

--Building Classification

insert into eg_module (id,name,enabled,contextroot, parentmodule,displayname,ordernumber) values (nextval('SEQ_EG_MODULE'),'BuildingClassification',true,null,
(select id from eg_module where name='PTIS-Masters'),'Building Classification',null);

--Search Classification
insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SearchStructureClassification','/structureclassification/search',null,
(select id from eg_module where name='BuildingClassification'),1,'Modify Building Classification',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='PTIS-Masters'));


--Feature Mapping

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Building Classification','Master screen to edit Structure Classification',
(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchStructureClassification'),(select id FROM eg_feature  WHERE name = 'Edit Building Classification'));


----Edit Classification

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'EditStructureClassification','/structureclassification/edit/',null,
(select id from eg_module where name='BuildingClassification'),1,'Edit Building Classification',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));



--Update View and Create Building classification

update eg_action set parentmodule=(select id from eg_module where name='BuildingClassification') where name='ViewStructureClassification';
update eg_action set parentmodule=(select id from eg_module where name='BuildingClassification') where name='AddStructureClassification';



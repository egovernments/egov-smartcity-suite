-----Creating Action Add Building Classification

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'AddStructureClassification','/structureclassification/create',null,
(select id from eg_module where name='PTIS-Masters'),1,'Add Building Classification',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AddStructureClassification'),
id from eg_role where name in ('Super User','Property Approver');


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add Building Classification','Master screen to Add Structure Classification',
(select id from eg_module  where name = 'Property Tax'));


INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AddStructureClassification'),(select id FROM eg_feature  WHERE name = 'Add Building Classification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Add Building Classification'));



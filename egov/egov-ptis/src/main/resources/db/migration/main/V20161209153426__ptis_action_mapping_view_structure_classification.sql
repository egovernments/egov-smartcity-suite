-----Creating Action

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'ViewStructureClassification','/structureclassification/view',null,
(select id from eg_module where name='PTIS-Masters'),1,'View Building Classification',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

------Assigning Action Roles to Users
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ViewStructureClassification'),id from eg_role where name in ('Super User','ULB Operator','VIEW_ACCESS_ROLE','Property Administrator','Property Verifier','Property Approver');

-----Feature Mapping
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Building Classification','Master screen to view Structure Classification',
(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewStructureClassification'),(select id FROM eg_feature  WHERE name = 'View Building Classification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Building Classification'));


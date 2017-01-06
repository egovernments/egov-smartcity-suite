----Creating New module Apartment/Complex Under Masters

insert into eg_module (id,name,enabled,contextroot, parentmodule,displayname,ordernumber) values (nextval('SEQ_EG_MODULE'),'Apartment/Complex',true,null,
(select id from eg_module where name='PTIS-Masters'),'Apartment/Complex',null);

----Search Apartment Action
insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SearchApartmentType','/apartment/search',null,
(select id from eg_module where name='Apartment/Complex'),1,'Search Apartment/Complex',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='PTIS-Masters'));

---Assigning Role

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'SearchApartmentType'),id from eg_role where name in ('Super User','ULB Operator','VIEW_ACCESS_ROLE','Property Administrator','Property Verifier','Property Approver');

----Feature Mapping

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Apartment/Complex','Master screen to Search Apartment Type',
(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchApartmentType'),(select id FROM eg_feature  WHERE name = 'Apartment/Complex'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Apartment/Complex'));



---View Apartment Type Action 

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'ApartmentType','/apartment/view',null,
(select id from eg_module where name='Apartment/Complex'),1,'View Apartment/Complex',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='PTIS-Masters'));

---Assigning Role

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ApartmentType'),id from eg_role where name in ('Super User','ULB Operator','VIEW_ACCESS_ROLE','Property Administrator','Property Verifier','Property Approver');

---Feature Mapping

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Apartment/Complex','Master screen to view Apartment Type',
(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'ApartmentType'),(select id FROM eg_feature  WHERE name = 'View Apartment/Complex'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Apartment/Complex'));


-----Create Apartment Action

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AddApartmentType','/apartment/create',null,
(select id from eg_module where name='Apartment/Complex'),1,'Add Apartment/Complex',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='PTIS-Masters'));

------Assigning Roles 
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AddApartmentType'),
id from eg_role where name in ('Super User','Property Approver');

----Feature Mapping

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add Apartment/Complex','Master screen to Add Apartment/Complex',
(select id from eg_module  where name = 'Property Tax'));


INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AddApartmentType'),(select id FROM eg_feature  WHERE name = 'Add Apartment/Complex'));



INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Add Apartment/Complex'));






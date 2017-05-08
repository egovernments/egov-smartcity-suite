
-- base Register report
delete from eg_feature_action where feature=(select id FROM EG_FEATURE WHERE name  ='Base Register Report');
delete from eg_feature_role where feature=(select id FROM EG_FEATURE WHERE name  ='Base Register Report');
delete from eg_feature where name='Base Register Report' and module =(select id from eg_module where name='Trade License');

INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Base Register Report','Base Register Report',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='baseregistersearchresult'),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='baseregistersearch'),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='BaseRegisterReport'),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));

-- closure
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Closure','License Closure',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='showclosureform'),(select id FROM EG_FEATURE
WHERE name  ='Closure'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Closure'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Closure'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLCreator')),(select id FROM EG_FEATURE
WHERE name  ='Closure'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLApprover')),(select id FROM EG_FEATURE
WHERE name  ='Closure'));

--Generate Demand Notice
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Generate License Demand Notice','Generate License Demand Notice',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='tldemandnoticereport'),(select id FROM EG_FEATURE
WHERE name  ='Generate License Demand Notice'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Generate License Demand Notice'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Generate License Demand Notice'));

--Generate Bulk Demand Notice
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Generate Bulk Demand Notice','Generate Bulk Demand Notice',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='generate tl demand notice pdf'),(select id FROM EG_FEATURE
WHERE name  ='Generate Bulk Demand Notice'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search TL For Bulk Demand Notice'),(select id FROM EG_FEATURE
WHERE name  ='Generate Bulk Demand Notice'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search TL For Bulk Demand Notice generation'),(select id FROM EG_FEATURE
WHERE name  ='Generate Bulk Demand Notice'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Generate Bulk Demand Notice'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Generate Bulk Demand Notice'));

--Create License SubCategory
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View License SubCategory'),(select id FROM EG_FEATURE
WHERE name  ='Create License Sub Category'));

--Modify License Subcategory
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View License SubCategory'),(select id FROM EG_FEATURE
WHERE name  ='Modify License Sub Category'));

--View License Subcategory
delete from eg_feature_action  where action=(select id FROM eg_action WHERE name ='Create License SubCategory') and feature=(select id FROM EG_FEATURE
WHERE name  ='View License Sub Category');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View License SubCategory'),(select id FROM EG_FEATURE
WHERE name  ='View License Sub Category'));

--Create Unit Of Measurement
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'Create Unit of Measurement');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'Create Unit of Measurement');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Create Unit Of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='Create Unit of Measurement'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View Unit Of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='Create Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));


--Modify Unit of Measurement
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Create Unit Of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='Modify Unit of Measurement'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Modify Unit of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='Modify Unit of Measurement'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View Unit Of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));

--View Unit of Measurement
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'View Unit of Measurement');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'View Unit of Measurement');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View Unit Of Measurement'),(select id FROM EG_FEATURE
WHERE name  ='View Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View Unit of Measurement'));

--Modify License FeeMatrix
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'Modify License FeeMatrix');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'Modify License FeeMatrix');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Update-FeeMatrix'),(select id FROM EG_FEATURE
WHERE name  ='Modify License FeeMatrix'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search-FeeMatrix'),(select id FROM EG_FEATURE
WHERE name  ='Modify License FeeMatrix'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View-FeeMatrix'),(select id FROM EG_FEATURE
WHERE name  ='Modify License FeeMatrix'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Modify License FeeMatrix'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Modify License FeeMatrix'));

--Create License FeeMatrix
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'Create License Fee Matrix');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'Create License Fee Matrix');
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Create-License FeeMatrix'),(select id FROM EG_FEATURE
WHERE name  ='Create License Fee Matrix'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View-FeeMatrix'),(select id FROM EG_FEATURE
WHERE name  ='Create License Fee Matrix'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Create License Fee Matrix'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Create License Fee Matrix'));

--Search Document Type
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name ='Search Document Type');
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name ='Search Document Type'));

--------------------------roleaction Mapping-------------------------------------------------

--View License Fee Matrix
delete from eg_roleaction where roleid=(select id from eg_role where name='TLCreator') and actionid=(select id from eg_action where name='View-FeeMatrix');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLApprover') and actionid=(select id from eg_action where name='View-FeeMatrix');
Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='View-FeeMatrix'));
Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='View-FeeMatrix'));

--Update-FeeMatrix
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='Update-FeeMatrix');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLCreator') and actionid=(select id from eg_action where name='Update-FeeMatrix');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLApprover') and actionid=(select id from eg_action where name='Update-FeeMatrix');
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Update-FeeMatrix'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Update-FeeMatrix'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Update-FeeMatrix'));

--Create-License FeeMatrix
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='Create-License FeeMatrix');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLApprover') and actionid=(select id from eg_action where name='Create-License FeeMatrix');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLCreator') and actionid=(select id from eg_action where name='Create-License FeeMatrix');
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Create-License FeeMatrix'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Create-License FeeMatrix'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Create-License FeeMatrix'));

--base register 
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='baseregistersearchresult');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='baseregistersearch');
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='baseregistersearchresult'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='baseregistersearch'));

--Modify Unit Of Measurement
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='Modify Unit Of Measurement');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLApprover') and actionid=(select id from eg_action where name='Modify Unit Of Measurement');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLCreator') and actionid=(select id from eg_action where name='Modify Unit Of Measurement');
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='Modify Unit Of Measurement' and contextroot = 'tl';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLCreator'), id from eg_action where name='Modify Unit Of Measurement' and contextroot = 'tl';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLAdmin'), id from eg_action where name='Modify Unit Of Measurement' and contextroot = 'tl';

--Create Unit of Measurement
delete from eg_roleaction where roleid=(select id from eg_role where name='TLAdmin') and actionid=(select id from eg_action where name='Create Unit Of Measurement');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLApprover') and actionid=(select id from eg_action where name='Create Unit Of Measurement');
delete from eg_roleaction where roleid=(select id from eg_role where name='TLCreator') and actionid=(select id from eg_action where name='Create Unit Of Measurement');
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Create Unit Of Measurement'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Create Unit Of Measurement'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Create Unit Of Measurement'));

--subcategory
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Create License SubCategory'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='View License SubCategory'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Modify License SubCategory'));

Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Create License SubCategory'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='View License SubCategory'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Modify License SubCategory'));

--UOM
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='View Unit Of Measurement'));
Insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='View Unit Of Measurement'));


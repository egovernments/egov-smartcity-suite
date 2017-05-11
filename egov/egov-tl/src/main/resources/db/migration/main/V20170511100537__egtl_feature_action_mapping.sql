
--Modify License Feematrix
delete from eg_feature_action where feature=(select id FROM EG_FEATURE WHERE name  ='Modify License FeeMatrix');
delete from eg_feature_role where role=(select id from eg_role where name='Super User') and feature=(select id FROM eg_feature WHERE name = 'Modify License FeeMatrix');
delete from eg_feature_role where role=(select id from eg_role where name='TLAdmin') and feature=(select id FROM eg_feature WHERE name = 'Modify License FeeMatrix');
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Modify License FeeMatrix','Modify License FeeMatrix',(select id from eg_module where name='Trade License'),0);
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

--Modify Document Type
delete from eg_feature_action where action=(select id FROM eg_action WHERE name ='Edit Document Type') and feature=(select id FROM EG_FEATURE
WHERE name  ='Search Document Type');
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'Modify License Document Type','Modify License Document Type',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search License Document Type'),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Edit Document Type'),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Delete Document Type'),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View Document Type'),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Modify License Document Type'));

--Modify Unit of Measurement
delete from eg_feature_action  where action=(select id FROM eg_action WHERE name ='Create Unit Of Measurement') and 
feature=(select id FROM EG_FEATURE WHERE name  ='Modify Unit of Measurement');
UPDATE EG_FEATURE_ACTION set action=(select id FROM eg_action WHERE name ='Modify Unit Of Measurement') where
 action is null and feature=(select id FROM EG_FEATURE WHERE name  ='Modify Unit of Measurement');

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLApprover') ,(select id FROM eg_feature WHERE name ='Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name ='Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLApprover') ,(select id FROM eg_feature WHERE name ='Create Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name ='Create Unit of Measurement'));

--base Register Grand total
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='BaseReportTotal'),(select id FROM EG_FEATURE
WHERE name  ='Base Register Report'));
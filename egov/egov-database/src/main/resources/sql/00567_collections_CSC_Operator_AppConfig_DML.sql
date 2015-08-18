INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDEPARTMENTFORWORKFLOW', 
'Department for Workflow',0, (select id from eg_module where name='Collection')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDEPARTMENTFORWORKFLOW' AND MODULE =(select id from eg_module where name='Collection')),current_date, 'Revenue',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONROLEFORNONEMPLOYEE', 
'roles for Collection workflow',0, (select id from eg_module where name='Collection')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONROLEFORNONEMPLOYEE'),current_date, 'CSC Operator',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDESIGNATIONFORCSCOPERATORASCLERK', 
'Designation for Collection workflow',0, (select id from eg_module where name='Collection')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDESIGNATIONFORCSCOPERATORASCLERK'),current_date, 'Revenue Clerk',0);


insert into eg_wf_types 
(id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version)
values 
(nextval('seq_eg_wf_types'),(select id from eg_module where name='Collection'),'ReceiptHeader','/collection/receipts/collectionsWorkflow-listWorkflow.action',1,now(),1,now(), 'Y', 'N', 'org.egov.collection.entity.ReceiptHeader', 'Collections Receipt Header', 0 );


delete from eg_feature_role where feature in(select id from eg_feature where name in('Create WaterTax Connection','DataEntry For WaterTax','Collection WaterTax','Create Close/Re Connection','ajax/Modify WaterTax Connection') and module in(select id from eg_module  where name = 'Water Tax Management'));

delete from eg_feature_action where feature in(select id from eg_feature where name in('Create WaterTax Connection','DataEntry For WaterTax','Collection WaterTax','Create Close/Re Connection','ajax/Modify WaterTax Connection') and module in(select id from eg_module  where name = 'Water Tax Management'));

delete from eg_feature where name in('Create WaterTax Connection','DataEntry For WaterTax','Collection WaterTax','Create Close/Re Connection','ajax/Modify WaterTax Connection') and module in(select id from eg_module  where name = 'Water Tax Management');

--new connection

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax NewConnection','Create WaterTax NewConnection',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnectionNewForm') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));




INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

--additional connection

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax AdditionalConnection','Create WaterTax AdditionalConnection',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddtionalWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnectionNewForm') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));


--changeof usage



INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax ChangeOfUseConnection','Create WaterTax ChangeOfUseConnection',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnectionNewForm') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxChangeOfUseApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateChangeOfUseConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection'));

--closure conn


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax ClosureConnection','Create WaterTax ClosureConnection',
(select id from eg_module  where name = 'Water Tax Management')); 



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createClosureConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ClosureConnection'));

--Reconnection


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax ReConnConnection','Create WaterTax ReConnection',
(select id from eg_module  where name = 'Water Tax Management')); 



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createReConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterReConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select 
id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

--search connection


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'WaterTax SearchConnection','WaterTax SearchConnection',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'watertaxappsearch') ,(select id FROM eg_feature WHERE name = 'WaterTax SearchConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'WaterTax SearchConnection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'WaterTax SearchConnection'));


--data entry


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'DataEntry For WaterTax','DataEntry For WaterTax',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxConnectionDataEntry') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifydataentryconnectiondetails') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxForExistingConsumerCodeFordataEntry') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editdataEntryDemand') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));


--enter meter



INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Enter Meter Reading','Enter Meter Reading',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EnterMeterEntryForConnection') ,(select id FROM eg_feature WHERE name = 'Enter Meter Reading'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Enter Meter Reading'));

--watertax collecton






INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'WaterTax Collection','WaterTax Collection',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'collectTaxForwatrtax') ,(select id FROM eg_feature WHERE name = 'WaterTax Collection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'WaterTax Collection'));


--update connection


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update WaterTax Connection','Update WaterTax Connection',
(select id from eg_module  where name = 'Water Tax Management')); 




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select 
id FROM eg_feature WHERE name = 'Update WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));




INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));




INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));


---data entry edit collection


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'DataEntry Edit Collection','DataEntry Edit Collection',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EditCollection') ,(select id FROM eg_feature WHERE name = 'DataEntry Edit Collection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editdataEntryCollection') ,(select id FROM eg_feature WHERE name = 'DataEntry Edit Collection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'DataEntry Edit Collection'));

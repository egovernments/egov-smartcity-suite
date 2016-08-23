
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'DataEntry For WaterTax','DataEntry For WaterTax',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterTax Connection','Create WaterTax Connection',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collection WaterTax','Collection WaterTax',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Close/Re Connection','Create Close/Re Connection',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'ajax/Modify WaterTax Connection','ajax/Modify WaterTax Connection',(select id from eg_module  where name = 'Water Tax Management')); 

--create data Entry
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxConnectionDataEntry') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));


---only CSC and ulb Operator perticular actions

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnectionNewForm') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddtionalWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxChangeOfUseApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateAdditionalConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateChangeOfUseConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));


---only ulb Operator perticular actions

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create Close/Re Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'meterEntryScreen') ,(select id FROM eg_feature WHERE name = 'Create Close/Re Connection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterReConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create Close/Re Connection'));



---modify and ajax url

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'watertaxappsearch') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EditCollection') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifydataentryconnectiondetails') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EnterMeterEntryForConnection') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createReConnection') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createClosureConnection') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateBillForConsumerCode') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'watertaxappsearch') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

--collection user

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'collectTaxForwatrtax') ,(select id FROM eg_feature WHERE name = 'Collection WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'watertaxappsearch') ,(select id FROM eg_feature WHERE name = 'Collection WaterTax'));



--feature role
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Close/Re Connection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Collection WaterTax'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax Connection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));


--date entry feature

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'ajax/Modify WaterTax Connection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));

--


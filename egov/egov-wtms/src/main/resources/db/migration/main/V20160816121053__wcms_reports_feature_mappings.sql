
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'WaterTax Reports Feature','WaterTax Reports Feature',
(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxDCBReportZoneWise') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxDCBReportWardWise') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxDCBReportBlockWise') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxDCBReportLocalityWise') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DCBReportList') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxConnectionReportWardWise') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxConnectionReportWardWiseupdate') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DailyWTCollectionReport') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateBillReport-ajax') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DefaultersReport-ajax') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'BaseRegister Report result') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'BaseRegister Report') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateBillForConsumerCode') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Water Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

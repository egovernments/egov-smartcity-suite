INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateBillReport') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DataEntryConnectionReport') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DataEntryConnectionReport-ajax') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

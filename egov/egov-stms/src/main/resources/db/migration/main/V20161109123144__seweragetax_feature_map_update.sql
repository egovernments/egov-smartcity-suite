INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetSewerageLegacyDonationAmount') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateSewerageTaxDemandBill') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewSewerageCloseConnectionNotice') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));



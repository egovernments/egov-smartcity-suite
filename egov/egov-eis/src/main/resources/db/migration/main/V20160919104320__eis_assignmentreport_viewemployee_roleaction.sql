INSERT INTO eg_roleaction VALUES((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='View Employee' and contextroot = 'eis'));
INSERT INTO eg_roleaction VALUES((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='View Employee' and contextroot = 'eis'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Employee') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));



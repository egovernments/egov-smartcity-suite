delete from eg_roleaction where actionid in (select id from eg_action where name in ('ajax edit watertax donation amount') and contextroot = 'wtms') and roleid in (select id from eg_role where name = 'CSC Operator');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='ajax edit watertax donation amount'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));

--rollback delete from eg_feature_role where feature in ((select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'),(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'),(select id FROM eg_feature WHERE name = 'Update WaterTax Connection')) and role = (select id from eg_role where name = 'CSC Operator');

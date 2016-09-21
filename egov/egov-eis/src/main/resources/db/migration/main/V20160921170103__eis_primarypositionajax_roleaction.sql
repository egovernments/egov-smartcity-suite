INSERT INTO eg_action values(nextval('seq_eg_action'),'EmpPrimaryPosition','/employee/ajax/primaryPosition','',(select id from eg_module where name='Employee'),null,'EmpPrimaryPosition',false,'eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='EmpPrimaryPosition'));

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'EmpPrimaryPosition'), id from eg_role where name in ('Employee Admin');

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpPrimaryPosition') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpPrimaryPosition') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));
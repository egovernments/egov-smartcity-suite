-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Employee','Create Employee',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify/View Employee','Modify/View Employee',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create/Modify Designation','Create/Modify Designation',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Designation','View Designation',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Position','Create Position',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Position','Create Position',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Employee Data Entry','Create Employee Data',(select id from eg_module  where name = 'EIS'));

-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------

-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Employee') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundarys') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpDesigAutoComplete') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpPosAutoComplete') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Employee') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Employee') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpSearchAjax') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundarys') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpDesigAutoComplete') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpPosAutoComplete') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Employee') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Designation') ,(select id FROM eg_feature WHERE name = 'Create/Modify Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Designation') ,(select id FROM eg_feature WHERE name = 'Create/Modify Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Designation') ,(select id FROM eg_feature WHERE name = 'View Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load designation') ,(select id FROM eg_feature WHERE name = 'View Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Position') ,(select id FROM eg_feature WHERE name = 'Create Position'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Position') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Position count in Search Position') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Position') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax Call in Search Position') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Employee Data') ,(select id FROM eg_feature WHERE name = 'Employee Data Entry'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EmpDesigAutoComplete') ,(select id FROM eg_feature WHERE name = 'Employee Data Entry'));

------------------------------------------------------------ADDING FEATURE ACTION ENDS-----------------------------------------------------


------------------------------------------------------------ADDING FEATURE ROLE STARTS-----------------------------------------------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Create Employee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Modify/View Employee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create/Modify Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Position'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Create Position'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Search Position'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Employee Data Entry'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Employee Data Entry'));

----------------------------------------------------------ADDING FEATURE ROLE ENDS---------------------------------------------------------
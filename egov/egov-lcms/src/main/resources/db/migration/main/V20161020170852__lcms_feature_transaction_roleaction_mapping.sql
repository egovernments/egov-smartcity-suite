------------------------------------ADDING FEATURE STARTS------------------------
----Create Legal Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Legal Case','Create Legal Case',(select id from EG_MODULE where name = 'LCMS'));

----Search Legal Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Legal Case','Search Legal Case',(select id from EG_MODULE where name = 'LCMS'));

---Edit Legal Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Legal Case','Edit Legal Case',(select id from EG_MODULE where name = 'LCMS'));

---View Legal Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Legal Case','View Legal Case',(select id from EG_MODULE where name = 'LCMS'));

----Create Judgment 
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Judgment','Create Judgment',(select id from EG_MODULE where name = 'LCMS'));

---Edit Judgment
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Judgment','Edit Judgment',(select id from EG_MODULE where name = 'LCMS'));

---Hearing
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Hearing','Hearing Details Screen',(select id from EG_MODULE where name = 'LCMS'));

---LcInterim Order
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Interim Order','Interim Order Screen',(select id from EG_MODULE where name = 'LCMS'));

---Add/Edit Standing Counsel
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add/Edit Standing Counsel','Add/Edit Standing Counsel',(select id from EG_MODULE where name = 'LCMS'));

---Add/Edit Counter Affidavit Details
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add/Edit Counter Affidavit Details','Add/Edit Counter Affidavit Details',(select id from EG_MODULE where name = 'LCMS'));

---Add/Edit Judgment Implementation
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Judgment Implementation','Judgment Implementation',(select id from EG_MODULE where name = 'LCMS'));

---Close Legal Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Close Case','Close Legal Case',(select id from EG_MODULE where name = 'LCMS'));

---Edit Close Case
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Close Case','Edit Close Legal Case',(select id from EG_MODULE where name = 'LCMS'));

------------------------------------ADDING FEATURE ACTION STARTS------------------------
----Create Legal Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createlegalcase') ,(select id FROM eg_feature WHERE name = 'Create Legal Case'));

----Search Legal Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'searchlegalcase') ,(select id FROM eg_feature WHERE name = 'Search Legal Case'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'searchlegalcaseresult') ,(select id FROM eg_feature WHERE name = 'Search Legal Case'));

---Edit Legal Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editlegalcase') ,(select id FROM eg_feature WHERE name = 'Edit Legal Case'));

---View Legal Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewlegalcase') ,(select id FROM eg_feature WHERE name = 'View Legal Case'));

----Create Judgment 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-Judgment') ,(select id FROM eg_feature WHERE name = 'Create Judgment'));

---Edit Judgment
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-Judgment') ,(select id FROM eg_feature WHERE name = 'Edit Judgment'));

---Hearing
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-Hearing') ,(select id FROM eg_feature WHERE name = 'Hearing'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-Hearing') ,(select id FROM eg_feature WHERE name = 'Hearing'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-Hearing') ,(select id FROM eg_feature WHERE name = 'Hearing'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'legalajaxforemployeeposition') ,(select id FROM eg_feature WHERE name = 'Hearing'));

---Interim Order
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-LcInterimOrde') ,(select id FROM eg_feature WHERE name = 'Interim Order'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View LcInterimOrder') ,(select id FROM eg_feature WHERE name = 'Interim Order'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'List LcInterimOrder') ,(select id FROM eg_feature WHERE name = 'Interim Order'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit LcInterimOrder') ,(select id FROM eg_feature WHERE name = 'Interim Order'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New Vacate Stay') ,(select id FROM eg_feature WHERE name = 'Interim Order'));

---Add/Edit Standing Counsel
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddStandingCouncil') ,(select id FROM eg_feature WHERE name = 'Add/Edit Standing Counsel'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'legalcaseAdvocateSearch') ,(select id FROM eg_feature WHERE name = 'Add/Edit Standing Counsel'));

---Add/Edit Counter Affidavit Details
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddCounterAffidavit') ,(select id FROM eg_feature WHERE name = 'Add/Edit Counter Affidavit Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'legalajaxforDepartment') ,(select id FROM eg_feature WHERE name = 'Add/Edit Counter Affidavit Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'legalajaxforposition') ,(select id FROM eg_feature WHERE name = 'Add/Edit Counter Affidavit Details'));

---Add/Edit Judgment Implementation
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-JudgmentImpl') ,(select id FROM eg_feature WHERE name = 'Judgment Implementation'));

---Close Legal Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-LCDisposal') ,(select id FROM eg_feature WHERE name = 'Close Case'));

---Edit Close Case
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-LCDisposal') ,(select id FROM eg_feature WHERE name = 'Edit Close Case'));

------------------------------------ADDING FEATURE ROLE STARTS--------------------------
----Create Legal Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Legal Case'));

----Search Legal Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Legal Case'));

---Edit Legal Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Edit Legal Case'));

---View Legal Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Legal Case'));

----Create Judgment 
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Judgment'));

---Edit Judgment
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Edit Judgment'));

---Hearing
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Hearing'));

---Interim Order
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Interim Order'));

---Add/Edit Standing Counsel
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Add/Edit Standing Counsel'));

---Add/Edit Counter Affidavit Details
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Add/Edit Counter Affidavit Details'));

---Add/Edit Judgment Implementation
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Judgment Implementation'));

---Close Legal Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Close Case'));

---Edit Close Case
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Edit Close Case'));

------------------------------End-------------------------------------------
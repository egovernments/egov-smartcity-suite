------------------Masters--------------------------------------
--------------------------ADDING FEATURE STARTS------------------------------
-----InterimOrder Type Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Interim Order','Create Interim Order',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Interim Order','View Interim Order',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Interim Order','Modify Interim Order',(select id from EG_MODULE where name = 'LCMS'));

------------CourtType Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Court Type','Create Court Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Court Type','View Court Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Court Type','Modify Court Type',(select id from EG_MODULE where name = 'LCMS'));

--------CaseType Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Case Type','Create  Case Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Case Type','View Case Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Case Type','Modify Case Type',(select id from EG_MODULE where name = 'LCMS'));

-------Court Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Court','Create Court',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Court','View Court',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Court','Modify Court',(select id from EG_MODULE where name = 'LCMS'));

-----PetitionType Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Petition Type','Create Petition Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Petition Type','View Petition Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Petition Type','Modify Petition Type',(select id from EG_MODULE where name = 'LCMS'));

-----JudgmentType Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Judgment Type','Create Judgment Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Judgment Type','View Judgment Type',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Judgment Type','Modify Judgment Type',(select id from EG_MODULE where name = 'LCMS'));

----GovernmentDepartment Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Government Department','Create Government Department',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Government Department','View Government Department',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Government Department','Modify Government Departmente',(select id from EG_MODULE where name = 'LCMS'));

---Standing Counsel Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Standing Counsel','Create Standing Counsel',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Standing Counsel',' View Standing Counsel',(select id from EG_MODULE where name = 'LCMS'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Standing Counsel','Modify Standing Counsel',(select id from EG_MODULE where name = 'LCMS'));



------------------------------------ADDING FEATURE ACTION STARTS------------------------
---Interim OrderType Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Interim Order'), id from eg_action where name  in('New-InterimOrder','Create-InterimOrder','Result-InterimOrder'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Interim Order'), id from eg_action where name  in('Search and View-InterimOrder','Search and View Result-InterimOrder','View-InterimOrder'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Interim Order'), id from eg_action where name  in('Search and Edit-InterimOrder','Search and Edit Result-InterimOrder','Edit-InterimOrder','Update-InterimOrder','Result-InterimOrder'); 

-------CourtType Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Court Type'), id from eg_action where name  in('New-CourttypeMaster','Create-CourttypeMaster','Result-CourttypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Court Type'), id from eg_action where name  in('View-CourttypeMaster','Search and View-CourttypeMaster','Search and View Result-CourttypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Court Type'), id from eg_action where name  in('Update-CourttypeMaster','Edit-CourttypeMaster','Search and Edit-CourttypeMaster','Search and Edit Result-CourttypeMaster','Result-CourttypeMaster'); 

------CaseType Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Case Type'), id from eg_action where name  in('New-CasetypeMaster','Create-CasetypeMaster','Result-CasetypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Case Type'), id from eg_action where name  in('Search and View-CasetypeMaster','View-CasetypeMaster','Search and View Result-CasetypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Case Type'), id from eg_action where name  in('Search and Edit-CasetypeMaster','Search and Edit Result-CasetypeMaster','Edit-CasetypeMaster','Update-CasetypeMaster','Result-CasetypeMaster'); 

-----Court Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Court'), id from eg_action where name  in('New-CourtMaster','Create-CourtMaster','Result-CourtMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Court'), id from eg_action where name  in('View-CourtMaster','Search and View-CourtMaster','Search and View Result-CourtMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Court'), id from eg_action where name  in('Update-CourtMaster','Edit-CourtMaster','Search and Edit-CourtMaster','Search and Edit Result-CourtMaster','Result-CourtMaster'); 

---PetitionType Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Petition Type'), id from eg_action where name  in('New-PetitionTypeMaster','Create-PetitionTypeMaster','Result-PetitionTypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Petition Type'), id from eg_action where name  in('View-PetitionTypeMaster','Search and View-PetitionTypeMaster','Search and View Result-PetitionTypeMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Petition Type'), id from eg_action where name  in('Update-PetitionTypeMaster','Edit-PetitionTypeMaster','Search and Edit-PetitionTypeMaster','Search and Edit Result-PetitionTypeMaster','Result-PetitionTypeMaster'); 

----JudgmentType Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Judgment Type'), id from eg_action where name  in('New-JudgmentType','Create-JudgmentType','Result-JudgmentType'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Judgment Type'), id from eg_action where name  in('View-JudgmentType','Search and View-JudgmentType','Search and View Result-JudgmentType'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Judgment Type'), id from eg_action where name  in('Update-JudgmentType','Edit-JudgmentType','Search and Edit-JudgmentType','Search and Edit Result-JudgmentType','Result-JudgmentType'); 

----Government Department Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Government Department'), id from eg_action where name  in('New-GovernmentDepartment','Create-GovernmentDepartment','Result-GovernmentDepartment'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Government Department'), id from eg_action where name  in('View-GovernmentDepartment','Search and View-GovernmentDepartment','Search and View Result-GovernmentDepartment'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Government Department'), id from eg_action where name  in('Update-GovernmentDepartment','Edit-GovernmentDepartment','Search and Edit-GovernmentDepartment','Search and Edit Result-GovernmentDepartment','Result-GovernmentDepartment'); 

-----Standing Counsel Master
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Standing Counsel'), id from eg_action where name  in('New-AdvocateMaster','Create-AdvocateMaster','Result-AdvocateMaster','BankBranchsByBankAjax'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Standing Counsel'), id from eg_action where name  in('View-AdvocateMaster','Search and View-AdvocateMaster','Search and View Result-AdvocateMaster'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Standing Counsel'), id from eg_action where name  in('Update-AdvocateMaster','Edit-AdvocateMaster','Search and Edit-AdvocateMaster','Search and Edit Result-AdvocateMaster','Result-AdvocateMaster','BankBranchsByBankAjax'); 
----------------------------------------------------------------------------------------------------------------------------------

------------------------------------ADDING FEATURE ROLE STARTS--------------------------
---Interim OrderType Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Interim Order'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Interim Order'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Interim Order'));

-----CourtType Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Court Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Court Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Court Type'));

----CaseType Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Case Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Case Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Case Type'));

-----Court Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Court'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Court'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Court'));

----PetitionType Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Petition Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Petition Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Petition Type'));

---JudgmentType Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Judgment Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Judgment Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Judgment Type'));

---Government Department master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Government Department'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Government Department'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Government Department'));

----Standing Counsel Master
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Standing Counsel'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Standing Counsel'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Standing Counsel'));

------------------------------------------------------End-------------------------------------------------------------------------------------

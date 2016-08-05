------------------------------------ADDING FEATURE STARTS------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Hierarchy Type','Create a Boundary Hierarchy Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Hierarchy Type','Modify an existing Boundary Hierarchy Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Hierarchy Type','View an existing Boundary Hierarchy Type',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Boundary Type','Create a Boundary Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Boundary Type','View an existing Boundary Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Boundary Type','Modify an existing Boundary Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Sub Boundary Type','Add a Sub-Boundary Type to an existing Boundary Type',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add/Modify Boundary','Search and Add/Modify Boundary',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cross Hierarchy','Cross Hierarchy Mapping for Location',(select id from eg_module  where name = 'Administration'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Role','Create a Role',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Role','Modify an existing Role',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Role','View an existing Role',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'User Role Assignment','Search and modify User Role Assignment',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Access Control By Feature','Grant Feature Access to Roles',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Reset Password','Master Password Resetting',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Configuration','Create a Configuration',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Configuration','Modify an existing Configuration',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Configuration','View an existing Configuration',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Department','Create a Department',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Department','Modify an existing Department',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Department','View an existing Department',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'City Setup','Overall City Setup',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Worklist and Drafts','Worklist and Drafts',(select id from eg_module  where name = 'Administration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Profile','Edit Profile for User',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Official Change Password','Change Password for User',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Feedback','Official sent Feedback',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Favourites','Add/Remove Favourites',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Common Administration','Common links for Administration Module',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Aadhaar Access','For Aadhaar Info Feature',(select id from eg_module  where name = 'Administration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Officials Home Page','Home Page for Officials',(select id from eg_module  where name = 'Administration'));
------------------------------------ADDING FEATURE ENDS------------------------

------------------------------------ADDING FEATURE ACTION STARTS------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateHierarchyTypeForm') ,(select id FROM eg_feature WHERE name = 'Create Hierarchy Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewHierarchyTypeForm') ,(select id FROM eg_feature WHERE name = 'Create Hierarchy Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateHierarchyTypeForm') ,(select id FROM eg_feature WHERE name = 'Create Hierarchy Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateHierarchyTypeForm') ,(select id FROM eg_feature  WHERE name = 'Modify Hierarchy Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewHierarchyTypeForm') ,(select id FROM eg_feature  WHERE name = 'Modify Hierarchy Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewHierarchyTypeForm'),(select id FROM eg_feature  WHERE name = 'View Hierarchy Type'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateBoundaryTypeForm') ,(select id FROM eg_feature  WHERE name = 'Create Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewBoundaryTypeForm') ,(select id FROM eg_feature  WHERE name = 'Create Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewBoundaryTypeForm') ,(select id FROM eg_feature  WHERE name = 'View Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundaryTypes') ,(select id FROM eg_feature  WHERE name = 'View Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateBoundaryTypeForm') ,(select id FROM eg_feature  WHERE name = 'Modify Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewBoundaryTypeForm') ,(select id FROM eg_feature  WHERE name = 'Modify Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundaryTypes') ,(select id FROM eg_feature  WHERE name = 'Modify Boundary Type'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AddChildBoundaryType') ,(select id FROM eg_feature  WHERE name = 'Create Sub Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundaryTypes') ,(select id FROM eg_feature  WHERE name = 'Create Sub Boundary Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxAddChildBoundaryTypeCheck') ,(select id FROM eg_feature  WHERE name = 'Create Sub Boundary Type'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchBoundaryForm') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundaryTypes') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateBoundary') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListBoundariesForm') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ShowCreateBoundary') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewBoundary') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateBoundary') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'modifyCrossHierarchy') ,(select id FROM eg_feature  WHERE name = 'Cross Hierarchy'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundarys') ,(select id FROM eg_feature  WHERE name = 'Cross Hierarchy'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateRoleForm') ,(select id FROM eg_feature WHERE name = 'Create Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Role View') ,(select id FROM eg_feature WHERE name = 'Create Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateRoleForm') ,(select id FROM eg_feature  WHERE name = 'Modify Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Role Update') ,(select id FROM eg_feature  WHERE name = 'Modify Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Role View') ,(select id FROM eg_feature  WHERE name = 'Modify Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewRoleForm'),(select id FROM eg_feature  WHERE name = 'View Role'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Role View'),(select id FROM eg_feature  WHERE name = 'View Role'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewuserRoleForm') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateuserRoleForm') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateuserRole') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadRoleByUser') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxuserlist') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewuserRole') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AccessControlByFeature') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListFeaturesByModule') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'GrantAccessControlByFeature') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AccessControlByRole') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RevokeAccessControlByRole') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ResetPassword') ,(select id FROM eg_feature  WHERE name = 'Reset Password'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateAppConfig') ,(select id FROM eg_feature WHERE name = 'Create Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppConfigModuleAutocomplete') ,(select id FROM eg_feature WHERE name = 'Create Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppconfigPopulateList') ,(select id FROM eg_feature  WHERE name = 'Create Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'modifyAppConfig') ,(select id FROM eg_feature  WHERE name = 'Modify Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppConfigModuleAutocomplete') ,(select id FROM eg_feature WHERE name = 'Modify Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppconfigPopulateList') ,(select id FROM eg_feature  WHERE name = 'Modify Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewAppConfig'),(select id FROM eg_feature  WHERE name = 'View Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppConfigModuleAutocomplete') ,(select id FROM eg_feature WHERE name = 'View Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppconfigPopulateList') ,(select id FROM eg_feature  WHERE name = 'View Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AppconfigValuesListForView'),(select id FROM eg_feature  WHERE name = 'View Configuration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewAppConfigAjaxResult'),(select id FROM eg_feature  WHERE name = 'View Configuration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'Create Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'Create Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'Create Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'Modify Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'Modify Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewDepartmentForm') ,(select id FROM eg_feature  WHERE name = 'View Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CitySetup') ,(select id FROM eg_feature  WHERE name = 'City Setup'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CitySetupUpdate') ,(select id FROM eg_feature  WHERE name = 'City Setup'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Inbox'),(select id FROM eg_feature  WHERE name = 'Worklist and Drafts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'InboxDraft') ,(select id FROM eg_feature  WHERE name = 'Worklist and Drafts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'InboxHistory') ,(select id FROM eg_feature  WHERE name = 'Worklist and Drafts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'OfficialsProfileEdit') ,(select id FROM eg_feature  WHERE name = 'Edit Profile'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'OfficialChangePassword') ,(select id FROM eg_feature  WHERE name = 'Official Change Password'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'OfficialSentFeedBack') ,(select id FROM eg_feature  WHERE name = 'Feedback'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AddFavourite') ,(select id FROM eg_feature  WHERE name = 'Favourites'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RemoveFavourite') ,(select id FROM eg_feature  WHERE name = 'Favourites'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Official Home Page') ,(select id FROM eg_feature  WHERE name = 'Officials Home Page'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'GisFileDownload') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature  WHERE name = 'Aadhaar Access'));



------------------------------------ADDING FEATURE ACTION ENDS------------------------


------------------------------------ADDING FEATURE ROLE STARTS------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Hierarchy Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Modify Hierarchy Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'View Hierarchy Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Create Boundary Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'View Boundary Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Modify Boundary Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Create Sub Boundary Type'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Add/Modify Boundary'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Cross Hierarchy'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Role'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Modify Role'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'View Role'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'User Role Assignment'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Access Control By Feature'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Reset Password'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Role'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Create Configuration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Modify Configuration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'View Configuration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Create Department'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Modify Department'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'View Department'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'City Setup'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Worklist and Drafts'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Edit Profile'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Official Change Password'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Favourites'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Aadhaar Access'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Feedback'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Employee') ,(select id FROM eg_feature  WHERE name = 'Officials Home Page'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Feedback'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Worklist and Drafts'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Edit Profile'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Official Change Password'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Favourites'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Common Administration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Aadhaar Access'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature  WHERE name = 'Officials Home Page'));




------------------------------------ADDING FEATURE ROLE ENDS------------------------



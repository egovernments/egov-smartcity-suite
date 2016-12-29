-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------

		-------------------------------------------Registration--------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Marriage Registration','Create Marriage Registration',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Marriage Registration','Search Marriage Registration',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify approved Marriage Registration','Modify approved Marriage Registration',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Marriage Registration','Update Marriage Registration',(select id from eg_module  where name = 'Marriage Registration'));

		------------------------------------------Reissue----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Reissue','Create Reissue',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Reissue','Update Reissue',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Reissue','View Reissue',(select id from eg_module  where name = 'Marriage Registration'));

		------------------------------------------data-entry----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create data entry','Create data entry',(select id from eg_module  where name = 'Marriage Registration'));

		------------------------------------------collect fee----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'collect fee','collect fee',(select id from eg_module  where name = 'Marriage Registration'));

		------------------------------------------Search Registration certificates----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Registration certificates','Search Registration certificates',(select id from eg_module  where name = 'Marriage Registration'));

-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------

				------------------------Registration -----------------

------------------------ Create -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateRegistration') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'show-mrregistrationunitzone') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'calculateMarriageFee') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

----------------------- view -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchRegistration') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Marriage Registration') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Marriage Registration Results') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

------------------------ modify approved -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Marriage Registration Results') ,(select id FROM eg_feature WHERE name = 'Modify approved Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Marriage Registration Record') ,(select id FROM eg_feature WHERE name = 'Modify approved Marriage Registration'));

------------------------ update -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationDataEntry') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'show-mrregistrationunitzone') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'calculateMarriageFee') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));


		------------------------------------------Reissue----------------------------

------------------------ create -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Re Issue Marriage Certifiate') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search register status MR records') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'show-mrregistrationunitzone') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

------------------------ update -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editReIssue') ,(select id FROM eg_feature WHERE name = 'Update Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editMrgCertificateReIssue') ,(select id FROM eg_feature WHERE name = 'Update Reissue'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'generateReIssueCertificate') ,(select id FROM eg_feature WHERE name = 'Update Reissue'));

------------------------ view -----------------


		------------------------------------------Data-entry----------------------------

------------------------ create -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'show-mrregistrationunitzone') ,(select id FROM eg_feature WHERE name = 'Create  data entry'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'mrgCertDataEntry') ,(select id FROM eg_feature WHERE name = 'Create data entry'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'checkUnqRegApplNo') ,(select id FROM eg_feature WHERE name = 'Create data entry'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'calculateMarriageFee') ,(select id FROM eg_feature WHERE name = 'Create data entry'));

		------------------------------------------collect -fee ----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search approved Registration') ,(select id FROM eg_feature WHERE name = 'collect fee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Marriage Registration Results') ,(select id FROM eg_feature WHERE name = 'collect fee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MarriageFeeBill') ,(select id FROM eg_feature WHERE name = 'collect fee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Collect Fee') ,(select id FROM eg_feature WHERE name = 'collect fee'));


		------------------------------------------ Search Registration Certificates ----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search Registration Certificates') ,(select id FROM eg_feature WHERE name = 'Search Registration certificates'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Print Marriage Certificates') ,(select id FROM eg_feature WHERE name = 'Search Registration certificates'));

-----------------------------------------------------------ADDING FEATURE ACTION ENDS----------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ROLE BEGINS----------------------------------------------------

		-------------------------------------------Registration--------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Create Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Approver') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Modify approved Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify approved Marriage Registration'));

		------------------------------------------Reissue----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Create Reissue'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Update Reissue'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Approver') ,(select id FROM eg_feature WHERE name = 'Update Reissue'));

		------------------------------------------data-entry----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create data entry'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Create data entry'));


		------------------------------------------collect fee----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'collect fee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'collect fee'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'collect fee'));

		------------------------------------------Search Registration certificates----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Registration certificates'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Search Registration certificates'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Creator') ,(select id FROM eg_feature WHERE name = 'Search Registration certificates'));
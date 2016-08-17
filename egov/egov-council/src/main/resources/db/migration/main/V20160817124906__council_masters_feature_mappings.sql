-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------

				------------------------ Council Member -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Council Member','Create Council Member',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Council Member','Update Council Member',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Council Member','View Council Member',(select id from eg_module  where name = 'Council Management'));

				------------------------ Council Designation -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Council Designation','Create Council Designation',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Council Designation','Update Council Designation',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Council Designation','View Council Designation',(select id from eg_module  where name = 'Council Management'));

				------------------------ Council Party -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Council Party','Create Council Party',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Council Party','Update Council Party',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Council Party','View Council Party',(select id from eg_module  where name = 'Council Management'));

				------------------------ Council Qualification -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Council Qualification','Create Council Qualification',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Council Qualification','Update Council Qualification',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Council Qualification','View Council Qualification',(select id from eg_module  where name = 'Council Management'));

				------------------------ Council Caste -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Council Caste','Create Council Caste',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Council Caste','Update Council Caste',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Council Caste','View Council Caste',(select id from eg_module  where name = 'Council Management'));

-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------

------------------------ Council Member -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Download-Photo') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

				 				    ------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilMember') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Download-Photo') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

								    ------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilMember') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilMember') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilMember') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Download-Photo') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

------------------------ Council Designation -----------------

                                	 ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Create Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Create Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Create Council Designation'));

				  					------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

				 					------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilDesignation') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

------------------------ Council Party -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Create Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Create Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Create Council Party'));

				  					------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilParty') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

									 ------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilParty') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilParty') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilParty') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

------------------------ Council Qualification -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Create Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Create Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Create Council Qualification'));

				  				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

				 					------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilQualification') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));


------------------------ Council Caste -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Create Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Create Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Create Council Caste'));

				  					------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

				 					------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilCaste') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));


-----------------------------------------------------------ADDING FEATURE ACTION ENDS----------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ROLE BEGINS----------------------------------------------------

									------------------------ Council Member -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Council Member'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Council Member'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Council Member'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Council Member'));


									------------------------ Council Designation -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Council Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Council Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Council Designation'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Council Designation'));

									------------------------ Council Party -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Council Party'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Council Party'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Council Party'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Council Party'));

									------------------------ Council Qualification -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Council Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Council Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Council Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Council Qualification'));

									------------------------ Council Caste -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Council Caste'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Council Caste'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Council Caste'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Council Caste'));

-----------------------------------------------------------ADDING FEATURE ROLE ENDS----------------------------------------------------
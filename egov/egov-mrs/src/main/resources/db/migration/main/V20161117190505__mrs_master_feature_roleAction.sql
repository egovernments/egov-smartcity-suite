-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------

				------------------------ Religion  -----------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Religion','Create Religion',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Religion','View Religion',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Religion','Update Religion',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));


				------------------------ Marriage Act  -----------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Act','Create Act',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Act','View Act',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Act','Update Act',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));


				------------------------ Marriage Registration unit  -----------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Registration unit','Create Registration unit',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Registration unit','Search and View-Registration unit',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Registration unit','Search and Edit-Registration unit',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));


				------------------------ Marriage Fee  -----------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Fee','Create Fee',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Fee','Update Fee',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Fee','View Fee',(SELECT id FROM eg_module WHERE name = 'Marriage Registration'));


-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------

				------------------------ Religion -----------------

                                  ------------------------ Create -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateReligion') ,(select id FROM eg_feature WHERE name = 'Create Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Religion Success Result') ,(select id FROM eg_feature WHERE name = 'Create Religion'));



				 ------------------------ Update -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Edit  Religion') ,(select id FROM eg_feature WHERE name = 'Update Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit Religion') ,(select id FROM eg_feature WHERE name = 'Update Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Religion') ,(select id FROM eg_feature WHERE name = 'Update Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Religion Search Result') ,(select id FROM eg_feature WHERE name = 'Update Religion'));


				------------------------ View -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And View Religion') ,(select id FROM eg_feature WHERE name = 'View Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Religion Search Result') ,(select id FROM eg_feature WHERE name = 'View Religion'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-Religion') ,(select id FROM eg_feature WHERE name = 'View Religion'));


				------------------------ Marriage Act  -----------------


                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Act') ,(select id FROM eg_feature WHERE name = 'Create Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Act Success Result') ,(select id FROM eg_feature WHERE name = 'Create Act'));


					 ------------------------ Update -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Edit  Act') ,(select id FROM eg_feature WHERE name = 'Update Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit Act') ,(select id FROM eg_feature WHERE name = 'Update Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Act') ,(select id FROM eg_feature WHERE name = 'Update Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Act Success Result') ,(select id FROM eg_feature WHERE name = 'Update Act'));



				------------------------ View -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And View Act') ,(select id FROM eg_feature WHERE name = 'View Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Act Search Result') ,(select id FROM eg_feature WHERE name = 'View Act'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-Act') ,(select id FROM eg_feature WHERE name = 'View Act'));

				------------------------ Marriage Registration unit  -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Registration unit') ,(select id FROM eg_feature WHERE name = 'Create Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-Registration unit') ,(select id FROM eg_feature WHERE name = 'Create Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-Registration unit') ,(select id FROM eg_feature WHERE name = 'Create Registration unit'));


					 ------------------------ Update -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-Registration unit') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result Registration unit') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-Registration unit') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'update-Registration unit') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));


				------------------------ View -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-Registration unit') ,(select id FROM eg_feature WHERE name = 'View Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-Registration unit') ,(select id FROM eg_feature WHERE name = 'View Registration unit'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result Registration unit') ,(select id FROM eg_feature WHERE name = 'View Registration unit'));



				  ------------------------ Marriage Fee  -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Fee') ,(select id FROM eg_feature WHERE name = 'Create Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Fee Success Result') ,(select id FROM eg_feature WHERE name = 'Create Fee'));


					 ------------------------ Update -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Show Edit  Fee') ,(select id FROM eg_feature WHERE name = 'Update Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Fee Success Result') ,(select id FROM eg_feature WHERE name = 'Update Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit Fee') ,(select id FROM eg_feature WHERE name = 'Update Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And Edit Result Fee') ,(select id FROM eg_feature WHERE name = 'Update Fee'));


				------------------------ View -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Fee Search Result') ,(select id FROM eg_feature WHERE name = 'View Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And View Fee') ,(select id FROM eg_feature WHERE name = 'View Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search And View Result Fee') ,(select id FROM eg_feature WHERE name = 'View Fee'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-Fee') ,(select id FROM eg_feature WHERE name = 'View Fee'));


-----------------------------------------------------------ADDING FEATURE ACTION ENDS----------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ROLE BEGINS----------------------------------------------------


				------------------------ Religion  -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Religion'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Religion'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Religion'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Create Religion'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'View Religion'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Update Religion'));


				------------------------ Marriage Act  -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Act'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Act'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Act'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Create Act'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'View Act'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Update Act'));



				------------------------ Marriage Registration unit  -----------------


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Registration unit'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Registration unit'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Create Registration unit'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'View Registration unit'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Update Registration unit'));


				------------------------ Marriage Fee  -----------------


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Fee'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Fee'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Fee'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Create Fee'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'View Fee'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration Admin') ,(select id FROM eg_feature WHERE name = 'Update Fee'));



-----------------------------------------------------------ADDING FEATURE ROLE ENDS----------------------------------------------------















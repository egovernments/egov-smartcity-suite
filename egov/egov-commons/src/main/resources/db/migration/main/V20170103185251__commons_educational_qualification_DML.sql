delete from eg_feature_role where feature in(select id from eg_feature where name in('Create Council Qualification','Update Council Qualification','View Council Qualification'));

delete  from eg_feature_action where  feature in(select id from eg_feature where name in('Create Council Qualification','Update Council Qualification','View Council Qualification'));

delete from eg_feature where name in('Create Council Qualification','Update Council Qualification','View Council Qualification');

delete from eg_roleaction where actionid in(select id from eg_action where name in('New-CouncilQualification','Create-CouncilQualification','Update-CouncilQualification','View-CouncilQualification','Edit-CouncilQualification','Result-CouncilQualification','Search and View-CouncilQualification','Search and Edit-CouncilQualification','Search and View Result-CouncilQualification','Search and Edit Result-CouncilQualification'));

delete from eg_action where name in('New-CouncilQualification','Create-CouncilQualification','Update-CouncilQualification','View-CouncilQualification','Edit-CouncilQualification','Result-CouncilQualification','Search and View-CouncilQualification','Search and Edit-CouncilQualification','Search and View Result-CouncilQualification','Search and Edit Result-CouncilQualification');

-------------alter table--------------------------------

ALTER TABLE egcncl_qualification RENAME TO eg_qualification;

ALTER TABLE eg_qualification RENAME CONSTRAINT pk_egcncl_qualification TO pk_eg_qualification;

ALTER SEQUENCE seq_egcncl_qualification RENAME TO seq_eg_qualification;


----------------------educational qualification role action mapping------------------------------------

INSERT into eg_module values (nextval('SEQ_EG_ACTION'),'CommonQualification',true,'common',(select id from eg_module where name='Common-Masters' and contextroot='common'),'Educational Qualification',4);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create Educational Qualification','/qualification/create',(select id from eg_module where name='CommonQualification'),1,'Create Educational Qualification',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Educational Qualification','/qualification/update',(select id from eg_module where name='CommonQualification'),2,'Update Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View Educational Qualification','/qualification/view',(select id from eg_module where name='CommonQualification'),3,'View Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit Educational Qualification','/qualification/edit',(select id from eg_module where name='CommonQualification'),4,'Edit Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result Educational Qualification','/qualification/result',(select id from eg_module where name='CommonQualification'),5,'Result Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Educational Qualification','/qualification/search/view',(select id from eg_module where name='CommonQualification'),6,'View Educational Qualification',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Educational Qualification','/qualification/search/edit',(select id from eg_module where name='CommonQualification'),7,'Update Educational Qualification',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result Educational Qualification','/qualification/searchresult/view',(select id from eg_module where name='CommonQualification'),8,'Search and View Result Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result Educational Qualification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result Educational Qualification','/qualification/searchresult/edit',(select id from eg_module where name='CommonQualification'),9,'Search and Edit Result Educational Qualification',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result Educational Qualification'));


--------------------------educational qualification feature action mapping----------------

                                ------------------------ Create -----------------
  
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Create Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Create Educational Qualification'));

				  				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Educational Qualification') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

				 					------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result Educational Qualification') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Educational Qualification') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Educational Qualification') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));


--------------------------educational qualification feature role mapping----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Educational Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Educational Qualification'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Educational Qualification'));

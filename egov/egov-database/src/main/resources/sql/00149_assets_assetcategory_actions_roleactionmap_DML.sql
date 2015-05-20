
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'CreateAssetCategory',null,null,now(),'/assetcategory/assetCategory-newform.action',null,null,(select id_module from eg_module where module_name='Asset Category'),1,'Create Asset Category','1',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ViewAssetCategory',null,null,now(),'/assetcategory/assetCategory-view.action',null,null,(select id_module from eg_module where module_name='Asset Category'),2,'View Asset Category','1',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ModifyAssetCategory',null,null,now(),'/assetcategory/assetCategory-edit.action',null,null,(select id_module from eg_module where module_name='Asset Category'),3,'Modify Asset Category','1',null,'egassets');

Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'SaveAssetCategory',null,null,now(),'/assetcategory/assetCategory-save.action',null,null,(select id_module from eg_module where module_name='Asset Category'),null,'SaveAssetCategory','0',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'SearchAssetCategoryResult',null,null,now(),'/assetcategory/assetCategory.action',null,null,(select id_module from eg_module where module_name='Asset Category'),null,'SearchAssetCategoryResult','0',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'EditAssetCategory',null,null,now(),'/assetcategory/assetCategory-showform.action',null,null,(select id_module from eg_module where module_name='Asset Category'),null,'EditAssetCategory','0',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'PopulateParentAjaxAsset',null,null,now(),'/assetcategory/ajaxAssetCategory-populateParentCategories.action',null,null,(select id_module from eg_module where module_name='Asset Category'),null,'PopulateParentAjaxAsset','0',null,'egassets');
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'PopulateParentDetailsAjaxAsset',null,null,now(),'/assetcategory/ajaxAssetCategory-populateParentDetails.action',null,null,(select id_module from eg_module where module_name='Asset Category'),null,'PopulateParentDetailsAjaxAsset','0',null,'egassets');


INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAssetCategory'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'CreateAssetCategory'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewAssetCategory'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ViewAssetCategory'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ModifyAssetCategory'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ModifyAssetCategory'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SaveAssetCategory'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SaveAssetCategory'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SearchAssetCategoryResult'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SearchAssetCategoryResult'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'EditAssetCategory'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'EditAssetCategory'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateParentAjaxAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateParentAjaxAsset'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateParentDetailsAjaxAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateParentDetailsAjaxAsset'));

--rollback delete from EG_ROLEACTION_MAP where ROLEID in('SUPER USER','Asset Administrator') and actionid in (select id FROM eg_action  WHERE context_root='egassets') ;
--rollback delete from eg_action where context_root='egassets';

Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'CreateAsset',null,null,now(),'/assetmaster/asset-newform.action',null,null,(select id from eg_module where name='Asset Master'),1,'Create Asset','true',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'CreateAsset'));



Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ViewAsset',null,null,now(),'/assetmaster/asset-view.action',null,null,(select id from eg_module where name='Asset Master'),2,'View Asset','true',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ViewAsset'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ModifyAsset',null,null,now(),'/assetmaster/asset-edit.action',null,null,(select id from eg_module where name='Asset Master'),3,'Modify Asset','true',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ModifyAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ModifyAsset'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AssetCreate',null,null,now(),'/assetmaster/asset-assetCreate.jsp',null,null,(select id from eg_module where name='Asset Master'),null,'Create Asset','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetCreate'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetCreate'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'SaveAsset',null,null,now(),'/assetmaster/asset-save.action',null,null,(select id from eg_module where name='Asset Master'),null,'SaveAsset','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SaveAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SaveAsset'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ShowAssetDetails',null,null,now(),'/assetmaster/asset-showform.action',null,null,(select id from eg_module where name='Asset Master'),null,'ShowAssetDetails','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ShowAssetDetails'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ShowAssetDetails'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ParentCatAjax',null,null,now(),'/assetmaster/ajaxAsset-populateParentCategories.action',null,null,(select id from eg_module where name='Asset Master'),null,'ParentCatAjax','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ParentCatAjax'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ParentCatAjax'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateWard',null,null,now(),'/assetmaster/ajaxAsset-populateWard.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateWard','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateWard'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateWard'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateArea',null,null,now(),'/assetmaster/ajaxAsset-populateArea.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateArea','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateArea'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateArea'));



Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateStreets',null,null,now(),'/assetmaster/ajaxAsset-populateStreets.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateStreets','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreets'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreets'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AssetList',null,null,now(),'/assetmaster/asset-list.action',null,null,(select id from eg_module where name='Asset Master'),null,'AssetList','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetList'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetList'));



Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AssetPopulateCategoryDetailsAjax',null,null,now(),'/assetmaster/ajaxAsset-populateCategoryDetails.action',null,null,(select id from eg_module where name='Asset Master'),null,'AssetPopulateCategoryDetailsAjax','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetPopulateCategoryDetailsAjax'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetPopulateCategoryDetailsAjax'));



Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateLocation',null,null,now(),'/assetmaster/ajaxAsset-populateLocations.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateLocation','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateLocation'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateLocation'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'PopulateSubCategoryAjaxAsset',null,null,now(),'/assetmaster/ajaxAsset-populateSubCategories.action',null,null,(select id from eg_module where name='Asset Master'),null,'PopulateSubCategoryAjaxAsset','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateSubCategoryAjaxAsset'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateSubCategoryAjaxAsset'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateAreaByLocation',null,null,now(),'/assetmaster/ajaxAsset-populateAreaByLocation.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateAreaByLocation','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateAreaByLocation'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateAreaByLocation'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'AjaxPopulateStreetsByLocation',null,null,now(),'/assetmaster/ajaxAsset-populateStreetsByLocation.action',null,null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateStreetsByLocation','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreetsByLocation'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreetsByLocation'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'PopulateLocationByAreaAjax',null,null,now(),'/assetmaster/ajaxAsset-populateLocationsByArea.action',null,null,(select id from eg_module where name='Asset Master'),null,'PopulateLocationByAreaAjax','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateLocationByAreaAjax'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateLocationByAreaAjax'));


Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,queryparams,urlorderid,module_id,order_number,display_name,is_enabled,action_help_url,context_root) values (nextval('seq_eg_action'),'ParentAssetCatAjax',null,null,now(),'/assetmaster/ajaxAsset-populateParentAssetCategoryList.action',null,null,(select id from eg_module where name='Asset Master'),null,'ParentAssetCatAjax','false',null,'egassets');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ParentAssetCatAjax'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ParentAssetCatAjax'));


--rollback delete from EG_ROLEACTION_MAP where ROLEID in (select id from eg_role where name in ('Super User','Asset Administrator')) and actionid in (select id FROM eg_action  WHERE context_root='egassets' and module_id in (select id from eg_module where name='Asset Master'));
--rollback delete from eg_action where context_root='egassets' and module_id in (select id from eg_module where name='Asset Master');


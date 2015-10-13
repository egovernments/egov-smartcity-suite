------------------START------------------
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Asset Management', 'true', 'egassets', null, 'Asset Management', null);
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Asset Category', 'true', null, (select id from eg_module where name='Asset Management'), 'Asset Category', 1);
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Asset Master', 'true', null, (select id from eg_module where name='Asset Management'), 'Asset Master', 2);
-------------------END-------------------


------------------START------------------
Insert into EG_ROLE (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version) values (nextval('seq_eg_role'),'Asset Administrator','Asset Administrator',now(),1,1,now(),0);
-------------------END-------------------

------------------START------------------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'CreateAssetCategory','/assetcategory/assetCategory-newform.action',null,(select id from eg_module where name='Asset Category'),1,'Create Asset Category','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ViewAssetCategory','/assetcategory/assetCategory-view.action',null,(select id from eg_module where name='Asset Category'),2,'View Asset Category','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ModifyAssetCategory','/assetcategory/assetCategory-edit.action',null,(select id from eg_module where name='Asset Category'),3,'Modify Asset Category','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'SaveAssetCategory','/assetcategory/assetCategory-save.action',null,(select id from eg_module where name='Asset Category'),null,'SaveAssetCategory','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'SearchAssetCategoryResult','/assetcategory/assetCategory.action',null,(select id from eg_module where name='Asset Category'),null,'SearchAssetCategoryResult','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'EditAssetCategory','/assetcategory/assetCategory-showform.action',null,(select id from eg_module where name='Asset Category'),null,'EditAssetCategory','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'PopulateParentAjaxAsset','/assetcategory/ajaxAssetCategory-populateParentCategories.action',null,(select id from eg_module where name='Asset Category'),null,'PopulateParentAjaxAsset','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'PopulateParentDetailsAjaxAsset','/assetcategory/ajaxAssetCategory-populateParentDetails.action',null,(select id from eg_module where name='Asset Category'),null,'PopulateParentDetailsAjaxAsset','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'CreateAsset','/assetmaster/asset-newform.action',null,(select id from eg_module where name='Asset Master'),1,'Create Asset','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ViewAsset','/assetmaster/asset-view.action',null,(select id from eg_module where name='Asset Master'),2,'View Asset','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ModifyAsset','/assetmaster/asset-edit.action',null,(select id from eg_module where name='Asset Master'),3,'Modify Asset','true','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AssetCreate','/assetmaster/asset-assetCreate.jsp',null,(select id from eg_module where name='Asset Master'),null,'Create Asset','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'SaveAsset','/assetmaster/asset-save.action',null,(select id from eg_module where name='Asset Master'),null,'SaveAsset','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ShowAssetDetails','/assetmaster/asset-showform.action',null,(select id from eg_module where name='Asset Master'),null,'ShowAssetDetails','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ParentCatAjax','/assetmaster/ajaxAsset-populateParentCategories.action',null,(select id from eg_module where name='Asset Master'),null,'ParentCatAjax','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateWard','/assetmaster/ajaxAsset-populateWard.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateWard','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateArea','/assetmaster/ajaxAsset-populateArea.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateArea','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateStreets','/assetmaster/ajaxAsset-populateStreets.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateStreets','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AssetList','/assetmaster/asset-list.action',null,(select id from eg_module where name='Asset Master'),null,'AssetList','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AssetPopulateCategoryDetailsAjax','/assetmaster/ajaxAsset-populateCategoryDetails.action',null,(select id from eg_module where name='Asset Master'),null,'AssetPopulateCategoryDetailsAjax','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateLocation','/assetmaster/ajaxAsset-populateLocations.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateLocation','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'PopulateSubCategoryAjaxAsset','/assetmaster/ajaxAsset-populateSubCategories.action',null,(select id from eg_module where name='Asset Master'),null,'PopulateSubCategoryAjaxAsset','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateAreaByLocation','/assetmaster/ajaxAsset-populateAreaByLocation.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateAreaByLocation','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'AjaxPopulateStreetsByLocation','/assetmaster/ajaxAsset-populateStreetsByLocation.action',null,(select id from eg_module where name='Asset Master'),null,'AjaxPopulateStreetsByLocation','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'PopulateLocationByAreaAjax','/assetmaster/ajaxAsset-populateLocationsByArea.action',null,(select id from eg_module where name='Asset Master'),null,'PopulateLocationByAreaAjax','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('seq_eg_action'),'ParentAssetCatAjax','/assetmaster/ajaxAsset-populateParentAssetCategoryList.action',null,(select id from eg_module where name='Asset Master'),null,'ParentAssetCatAjax','false','egassets',0,1,now(),1,now(),(select id from eg_module  where name = 'Asset Management'));
-------------------END-------------------


------------------START------------------
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAssetCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'CreateAssetCategory'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewAssetCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ViewAssetCategory'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ModifyAssetCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ModifyAssetCategory'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SaveAssetCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SaveAssetCategory'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SearchAssetCategoryResult'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SearchAssetCategoryResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'EditAssetCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'EditAssetCategory'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateParentAjaxAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateParentAjaxAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateParentDetailsAjaxAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateParentDetailsAjaxAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ParentAssetCatAjax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ParentAssetCatAjax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'CreateAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ViewAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ModifyAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ModifyAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetCreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'SaveAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'SaveAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ShowAssetDetails'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ShowAssetDetails'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ParentCatAjax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'ParentCatAjax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateWard'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateWard'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateArea'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateArea'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreets'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreets'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetList'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AssetPopulateCategoryDetailsAjax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AssetPopulateCategoryDetailsAjax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateLocation'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateLocation'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateSubCategoryAjaxAsset'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateSubCategoryAjaxAsset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateAreaByLocation'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateAreaByLocation'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreetsByLocation'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'AjaxPopulateStreetsByLocation'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PopulateLocationByAreaAjax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Asset Administrator') ,(select id FROM eg_action  WHERE name = 'PopulateLocationByAreaAjax'));
-------------------END-------------------

------------------START------------------
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (11,'Fixed Assets',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (15,'Accumulated Depreciation',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (17,'Depreciation Expense Account',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (18,'Revaluation Reserve Account',null,null,null,null);
-------------------END-------------------

------------------START------------------
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'IS_ASSET_CATEGORYCODE_AUTOGENERATED', 'Auto/User Generated Asset Category Code ', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ASSET_ACCOUNT_CODE_PURPOSEID', 'Asset Account Code Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'REVALUATION_RESERVE_ACCOUNT_PURPOSEID', 'Revaluation Reserve Account Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DEPRECIATION_EXPENSE_ACCOUNT_PURPOSEID', 'Depreciation Expense Account Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ACCUMULATED_DEPRECIATION_PURPOSEID', 'Accumulated Depreciation Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'IS_ASSET_CODE_AUTOGENERATED', 'Auto/User Generated Asset Code', (select id from eg_module where name='Asset Management'));
-------------------END-------------------

------------------START------------------
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Created',now(),'Created',1);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','CWIP',now(),'CWIP',2);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Capitalized',now(),'Capitalized',3);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Revaluated',now(),'Revaluated',4);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Disposed',now(),'Disposed',5);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Retired',now(),'Retired',6);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Sold',now(),'Sold',7);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Cancelled',now(),'CANCELLED',8);
-------------------END-------------------

------------------START------------------
DROP SEQUENCE SEQ_EG_SCRIPT;
CREATE SEQUENCE SEQ_EG_SCRIPT start with 8;

INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (nextval('seq_eg_script'), 'assets.assetcategorynumber.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getAssetCategoryCode():  
	sequenceName = ''ASSETCATEGORY_NUMBER'' 
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4) 
  	return runningNumber
result=getAssetCategoryCode()', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);


INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (nextval('seq_eg_script'), 'assets.assetnumber.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getAssetCode():  
	sequenceName = ''ASSET_''+asset.getAssetCategory().getCode()  
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4)  
	if(asset.getAssetCategory().getAssetType().toString().lower()=="movableasset".lower()):  
  		result=asset.getAssetCategory().getCode()+"/"+runningNumber+"/"+year  
	else:  
  		result=asset.getAssetCategory().getCode()+"/"+asset.getStreet().getBoundaryNum().toString()+"/"+runningNumber+"/"+year  
	return result  
result=getAssetCode()', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);
-------------------END-------------------


--rollback delete from eg_script where name='assets.assetnumber.generator';

--rollback delete from eg_script where name='assets.assetcategorynumber.generator';

--rollback delete from EGW_STATUS where moduletype='ASSET';

--rollback delete from eg_appconfig where module='Assets';

--rollback delete from egf_accountcode_purpose where name in('Fixed Assets','Accumulated Depreciation','Depreciation Expense Account','Revaluation Reserve Account');

--rollback delete from EG_ROLEACTION where actionid in (select id FROM eg_action  WHERE context_root='egassets');
--rollback delete from eg_action where context_root='egassets';

--rollback delete from EG_ROLE where name='Asset Administrator';
--rollback delete from eg_module where name in ('Asset Master','Asset Category','Asset Management');

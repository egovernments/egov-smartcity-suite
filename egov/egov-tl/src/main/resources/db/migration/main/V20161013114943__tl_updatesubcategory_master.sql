-----------------------------------update eg_roleaction----------------------------------------------------------

delete from eg_feature_action where action in (select id from eg_action where name in('Create License SubCategory','Modify License SubCategory','View License SubCategory','Save License SubCategory','saveEditedSubCategory') and contextroot='tl');

delete from eg_roleaction where actionid in (select id from eg_action where name='Save License SubCategory'); 
delete from eg_roleaction where actionid in (select id from eg_action where name='saveEditedSubCategory'); 

delete from eg_action where name='Save License SubCategory';
delete from eg_action where name='saveEditedSubCategory';

update eg_action set url='/licensesubcategory/create' where name='Create License SubCategory';
update eg_action set url='/licensesubcategory/update',QUERYPARAMS=null where name='Modify License SubCategory';
update eg_action set url='/licensesubcategory/view',QUERYPARAMS=null  where name='View License SubCategory';
update eg_action set url='/licensesubcategory/getsubcategories-by-category' where name='tradeLicenseSubCategoryAjax';

---------------------------update eg_feature_action-------------------------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Create License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Create License SubCategory'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Create License SubCategory'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Create License SubCategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Modify License SubCategory'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,
(select id FROM eg_feature WHERE name = 'Modify License SubCategory'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Modify License SubCategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License SubCategory') ,
(select id FROM eg_feature WHERE name = 'View License SubCategory'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,
(select id FROM eg_feature WHERE name = 'View License SubCategory'));





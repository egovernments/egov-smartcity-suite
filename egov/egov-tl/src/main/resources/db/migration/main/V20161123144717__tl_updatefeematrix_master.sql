delete from eg_roleaction where actionid in (select id from eg_action where name in ('Ajax-SubCategoryByParent','Ajax-loadUomName') and contextroot='tl'); 
delete from eg_feature_action where action in (select id from eg_action  WHERE name in ('Ajax-SubCategoryByParent','Ajax-loadUomName') and contextroot='tl');
delete from eg_action where name in ('Ajax-SubCategoryByParent','Ajax-loadUomName') and contextroot='tl';

update eg_action set url='/feeType/feetype-by-subcategory' where name='Ajax-FeeTypeBySubCategory' and contextroot='tl';
update eg_action set url='/feeType/uom-by-subcategory' where name='Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl';

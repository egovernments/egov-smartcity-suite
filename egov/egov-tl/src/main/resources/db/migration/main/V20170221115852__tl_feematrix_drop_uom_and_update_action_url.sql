update eg_action set url = '/licensesubcategory/by-category', name='LicenseSubcategoryByCategory' where name = 'tradeLicenseSubCategoryAjax' and contextroot='tl';
update eg_action set url = '/licensesubcategory/detail', name='LicenseSubcategoryDetailBySubcatgoryId' where name = 'Ajax-FeeTypeBySubCategory' and contextroot='tl';
update eg_action set url = '/licensesubcategory/detail-by-feetype', name='LicenseSubcategoryDetailByFeeType' where name = 'Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl';

ALTER TABLE egtl_feematrix DROP COLUMN unitofmeasurement;
ALTER TABLE egtl_feematrix ADD CONSTRAINT unq_egtl_feematrix UNIQUE(natureofbusiness,licenseapptype,licensecategory,subcategory,feetype,financialyear);

UPDATE eg_action SET queryparamregex='^licenseId=[1-9]\d*$' WHERE name='Trade License Bill Collect';
UPDATE eg_action SET queryparamregex='^model.id=[1-9]\d*$' WHERE name='View Trade License Generate Certificate';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='Trade License Renewal Notice');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='Trade License Renewal Notice');
DELETE FROM eg_action WHERE name='Trade License Renewal Notice';

UPDATE eg_action SET queryparamregex='^model.id=[1-9]\d*$' WHERE name='New Trade License Before Renew';
UPDATE eg_action SET queryparamregex='^id=[1-9]\d*$' WHERE name='viewTradeLicense-view';

UPDATE eg_action set name='TL_NEW_LICENSE_APPLICATION' WHERE name='Create New License';
UPDATE eg_action set name='TL_SEARCH_LICENSE' WHERE name='Search Trade License';
UPDATE eg_action set name='TL_SEARCH_LICENSE_AUTO_COMPLETE' WHERE name='Search License Autocomplete';
UPDATE eg_action set name='TL_CREATE_LEGACY_LICENSE' WHERE name='Enter Trade License Action';
UPDATE eg_action set name='TL_MODIFY_LEGACY_LICENSE' WHERE name='Modify-Legacy-License';
UPDATE eg_action set name='TL_LICENSE_BILL_COLLECT' WHERE name='Trade License Bill Collect';
UPDATE eg_action set name='TL_GENERATE_LICENSE_CERTIFICATE' WHERE name='View Trade License Generate Certificate';
UPDATE eg_action set name='TL_RENEW_LICENSE_APPLICATION' WHERE name='New Trade License Before Renew';

UPDATE eg_action set name='TL_CREATE_CATEGORY' WHERE name='Create License Category';
UPDATE eg_action set name='TL_MODIFY_CATEGORY' WHERE name='Modify License Category';
UPDATE eg_action set name='TL_VIEW_CATEGORY' WHERE name='View License Category';

UPDATE eg_action set name='TL_CREATE_SUBCATEGORY' WHERE name='Create License Subcategory';
UPDATE eg_action set name='TL_MODIFY_SUBCATEGORY' WHERE name='Modify License Subcategory';
UPDATE eg_action set name='TL_VIEW_SUBCATEGORY' WHERE name='View License Subcategory';

UPDATE eg_action set name='TL_VIEW_LICENSE_APPLICATION' WHERE name='viewTradeLicense-view';
UPDATE eg_action set name='TL_NEW_LICENSE_APPLICATION_SUBMIT' WHERE name='newTradeLicense-create';

UPDATE eg_action set name='TL_CREATE_UOM' WHERE name='Create Unit Of Measurement';
UPDATE eg_action set name='TL_MODIFY_UOM' WHERE name='Modify Unit Of Measurement';
UPDATE eg_action set name='TL_VIEW_UOM' WHERE name='View Unit Of Measurement';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='tradeLicenseAjaxMaster');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='tradeLicenseAjaxMaster');
DELETE FROM eg_action WHERE name='tradeLicenseAjaxMaster';

UPDATE eg_action set name='TL_NEW_LICENSE_APPROVAL' WHERE name='NewTradeLicense-approve';
UPDATE eg_action set name='TL_RENEW_LICENSE_APPLICATION_SUBMIT' WHERE name='NewTradeLicense-renewal';

UPDATE eg_action set name='TL_LICENSE_SUBCATEGORY_BY_CATEGORY' WHERE name='LicenseSubcategoryByCategory';
UPDATE eg_action set name='TL_LICENSE_SUBCATEGORY_DETAILS_BY_ID' WHERE name='LicenseSubcategoryDetailBySubcatgoryId';
UPDATE eg_action set name='TL_LICENSE_SUBCATEGORY_BY_FEETYPE' WHERE name='LicenseSubcategoryDetailByFeeType';

UPDATE eg_action set name='TL_SEARCH_FEEMATRIX' WHERE name='Search-FeeMatrix';
UPDATE eg_action set name='TL_CREATE_FEEMATRIX' WHERE name='Create-License FeeMatrix';
UPDATE eg_action set name='TL_MODIFY_FEEMATRIX' WHERE name='Update-FeeMatrix';
UPDATE eg_action set name='TL_VIEW_FEEMATRIX' WHERE name='View-FeeMatrix';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='Save Trade License');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='Save Trade License');
DELETE FROM eg_action WHERE name='Save Trade License';

UPDATE eg_action set name='TL_REPORT_VIEWER' WHERE name='TradeLicense report viewer';

UPDATE eg_action set name='TL_CREATE_VALIDITY' WHERE name='Create-Validity';
UPDATE eg_action set name='TL_MODIFY_VALIDITY' WHERE name='Update-Validity';
UPDATE eg_action set name='TL_VIEW_VALIDITY' WHERE name='View-Validity';
UPDATE eg_action set name='TL_SEARCH_VALIDITY' WHERE name='Search-Validity';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='Save-Modified-Legacy-License');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='Save-Modified-Legacy-License');
DELETE FROM eg_action WHERE name='Save-Modified-Legacy-License';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='penaltyratessearcheditresult');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='penaltyratessearcheditresult');
DELETE FROM eg_action WHERE name='penaltyratessearcheditresult';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='penaltyratessearchview');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='penaltyratessearchview');
DELETE FROM eg_action WHERE name='penaltyratessearchview';

DELETE FROM eg_roleaction WHERE ACTIONID=(SELECT id FROM eg_action WHERE name='penaltyratesview');
DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='penaltyratesview');
DELETE FROM eg_action WHERE name='penaltyratesview';

UPDATE eg_action set name='TL_CREATE_PENALTY_RATES',url='/penaltyrates/create',enabled=true WHERE name='penaltyratescreate';
UPDATE eg_action set name='TL_UPDATE_PENALTY_RATES',url='/penaltyrates/update/',enabled=false WHERE name='deletepenaltyrate';
UPDATE eg_action set name='TL_SEARCH_PENALTY_RATES',url='/penaltyrates/search',enabled=true WHERE name='penaltyratessearch';

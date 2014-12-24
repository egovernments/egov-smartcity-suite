
#UP

INSERT INTO EG_MODULE(ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
 (SEQ_MODULEMASTER.nextVal, 'LandAndEstateActions',sysdate,'0','',(select id_module from eg_module where module_name='LandAndEstate'),1,'LandEstate Actions');

Update EG_ACTION SET is_enabled = 1, module_id = (Select Id_module from eg_module where module_name='LandAndEstateActions')
where name IN ('createHoardingAgreement','modifyHoardingAgreement','viewHoardingAgreement','viewHoardingDetail','modifyHoardingDetail');


Update EG_ACTION SET is_enabled = 1, module_id = (Select Id_module from eg_module where module_name='LandAndEstateActions')
where name IN ('modifyShoppingComplex','viewShoppingComplex','addShopToShopingComplex');

Update EG_ACTION SET is_enabled = 1, module_id = (Select Id_module from eg_module where module_name='LandAndEstateActions')
where name IN ('viewShopDetail','modifyShopDetail','createShopAgreement','modifyShopAgreement','viewShopAgreement','cancelAgreement');

Update EG_ACTION SET is_enabled = 1, module_id = (Select Id_module from eg_module where module_name='LandAndEstateActions')
where name IN ('viewShopDetail','modifyShopDetail','createShopAgreement','modifyShopAgreement','viewShopAgreement','cancelAgreement','collectShopRent','ViewShopDCB');

Update EG_ACTION SET is_enabled = 1, module_id = (Select Id_module from eg_module where module_name='LandAndEstateActions')
where name IN ('viewLandDetail','modifyLandDetail','createLandAgreement','modifyLandAgreement','viewLandAgreement',
 'renewLandAgreement','landDemandPolicyChange','collectLandRent','ViewLandDCB');



#DOWN


Update EG_ACTION SET is_enabled = 0, module_id = (Select Id_module from eg_module where module_name='LandAndEstateMasters')
where name IN ('createHoardingAgreement','modifyHoardingAgreement','viewHoardingAgreement','viewHoardingDetail','modifyHoardingDetail');


Update EG_ACTION SET is_enabled = 0, module_id = (Select Id_module from eg_module where module_name='LandAndEstateMasters')
where name IN ('modifyShoppingComplex','viewShoppingComplex','addShopToShopingComplex');

Update EG_ACTION SET is_enabled = 0, module_id = (Select Id_module from eg_module where module_name='LandAndEstateMasters')
where name IN ('viewShopDetail','modifyShopDetail','createShopAgreement','modifyShopAgreement','viewShopAgreement','cancelAgreement');

Update EG_ACTION SET is_enabled = 0, module_id = (Select Id_module from eg_module where module_name='LandAndEstateMasters')
where name IN ('viewShopDetail','modifyShopDetail','createShopAgreement','modifyShopAgreement','viewShopAgreement','cancelAgreement','collectShopRent','ViewShopDCB');

Update EG_ACTION SET is_enabled = 0, module_id = (Select Id_module from eg_module where module_name='LandAndEstateMasters')
where name IN ('viewLandDetail','modifyLandDetail','createLandAgreement','modifyLandAgreement','viewLandAgreement',
 'renewLandAgreement','landDemandPolicyChange','collectLandRent','ViewLandDCB');

DELETE FROM eg_module WHERE module_name='LandAndEstateActions';

INSERT into eg_role values(nextval('seq_eg_role'),'Coll_Master Access','This role has access to create, view and modify all master screens',current_date,1,1,current_date,0); 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Details -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceDetails'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceDetailsCreate'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceDetailsList'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsView' ));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceDetailsBeforeCreate'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsView' ));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsModify' ));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeModify' ));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceDetailsSchemeList'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='AjaxCollectionsBankBranch'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxServiceLoadScheme' ));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxServiceLoadSubScheme' ));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='AjaxServiceCodeUniqueCheck'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='AjaxMiscReceiptFundSource'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ajax-process-function'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'ajax-process-coacodes'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Category -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceCategory'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceCategoryList'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceCategoryEdit'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceCategorySave'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceCategoryCreate'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Type To Bank Account Mapping -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceToBankMapping'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingCreate'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingList'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingSearch'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingEdit'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingSave'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='serviceListNotMappedToAccount'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='bankBranchsByBankForReceiptPayments'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Master Access'),(select id from eg_action where name='bankAccountByBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_Master Access') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


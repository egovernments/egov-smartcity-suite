
delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('CSC Operator')) and actionid in (select id FROM eg_action WHERE name in ('UpdateWaterConnectionApplication','AjaxDesignationsByDepartment', 'AjaxApproverByDesignationAndDepartment', 'WaterTaxCollectionView') and contextroot='wtms');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'View Water Connection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'watertaxappsearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ElasiticapplicationSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'Addtional Water Connection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxAddConnection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxChangeOfUseApplication'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCreateChangeOfUseApplication'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCollectionView'));



--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('ULB Operator')) and actionid in (select id FROM eg_action WHERE name in ('AjaxDesignationsByDepartment','AjaxApproverByDesignationAndDepartment', 'View Water Connection', 'UpdateWaterConnectionApplication', 'watertaxappsearch','ElasiticapplicationSearch', 'Addtional Water Connection', 'WaterTaxAddConnection', 'WaterTaxChangeOfUseApplication', 'WaterTaxCreateChangeOfUseApplication', 'WaterTaxCollectionView') and contextroot='wtms');

--rollback INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication' and contextroot='wtms'));
--rollback INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment' and contextroot='wtms'));
--rollback INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment' and contextroot='wtms'));
--rollback INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCollectionView' and contextroot='wtms'));


INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'viewSewerageConnectionDCBReport'),id from eg_role where name in ('ULB Operator');

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SewerageChangeInClosets' and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'SearchNotice'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'ShowNotice'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'STMergeAndDownload'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'STNoticeSearchResult'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'STNoticeSearchResultCount'));
 
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'STDailyCollectionReport'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator') , (select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'DCBReportWardwiseList'),id from eg_role where name in ('ULB Operator');
 
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator') , (select id from eg_action where name='Sewerage DCB Report View Connections'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SewerageConnectionChangeInClosetsValidation' and contextroot = 'stms'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SearchSewerageCharges' and contextroot = 'stms'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Change in Closet'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));



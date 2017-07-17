INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sewerage Tax Reports','Sewerage Tax ALL Reports',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageNoOfApplicationReportView') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage DCB Drill Down Report Wardwise') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STZipAndDownload') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STMergeAndDownload') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STNoticeSearchResultCount') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewSewerageConnectionDCBReport') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DCBReportWardwiseList') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage DCB Report View Connections') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchNotice') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STNoticeSearchResult') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ShowNotice') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageNoOfApplicationsReportSearch') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'Sewerage Tax Reports'));




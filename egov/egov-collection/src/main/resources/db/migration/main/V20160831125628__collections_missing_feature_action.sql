INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinMiscByService') ,(select id FROM eg_feature WHERE name = 'Create Challan'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceToBankMapping') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingList') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingCreate') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingSearch') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingEdit') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingSave') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'serviceListNotMappedToAccount') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankBranchsByBankForReceiptPayments') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankAccountByBankBranch') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionSummaryReport') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionSummaryReportResult') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeProcess') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-function') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));

DELETE FROM eg_feature_action  where feature in(select id from eg_feature  where name ='Online Transaction Report');

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReport') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReportResult') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));

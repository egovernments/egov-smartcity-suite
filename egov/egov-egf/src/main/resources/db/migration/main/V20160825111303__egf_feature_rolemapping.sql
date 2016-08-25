
--Cheque issue register
INSERT INTO eg_feature_action (FEATURE,ACTION ) values( (select id FROM eg_feature WHERE name = 'Cheque Issue Register'),(select id from eg_action where name  ='chequeIssueRegisterReport-bankAdviceExcel'));

--Cheque assignment
INSERT INTO eg_feature_action (FEATURE,ACTION )  values( (select id FROM eg_feature WHERE name = 'Cheque Assignment'),(select id from eg_action where name ='chequeAssignment-bankAdviceExcel'));

--Bank Advice For RTGS Payment
INSERT INTO eg_feature_action (FEATURE,ACTION )  values( (select id FROM eg_feature WHERE name = 'Bank Advice For RTGS Payment'),(select id from eg_action where name  ='bankAdviceReportExportExcel'));

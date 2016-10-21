------------------------------------ADDING FEATURE STARTS-------------------------------------
---Time Series Report
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Time Series Report','Time Series Report',(select id from EG_MODULE where name = 'LCMS'));

---Generic Sub Reports
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Generic Sub Reports','Generic Sub Reports',(select id from EG_MODULE where name = 'LCMS'));

---Daily Board Reports
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Daily Board Reports','Daily Board Reports',(select id from EG_MODULE where name = 'LCMS'));

---Reports Between Due Dates
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Reports Between Due Dates','Reports Between Due Dates',(select id from EG_MODULE where name = 'LCMS'));

------------------------------------ADDING FEATURE ACTION STARTS------------------------
---Time Series Report
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'TimeSeriesReports') ,(select id FROM eg_feature WHERE name = 'Time Series Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'TimeSeriesReportResult') ,(select id FROM eg_feature WHERE name = 'Time Series Report'));

---Generic Sub Reports
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenericSubReports') ,(select id FROM eg_feature WHERE name = 'Generic Sub Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenericSubReportResult') ,(select id FROM eg_feature WHERE name = 'Generic Sub Reports'));

---Daily Board Reports
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DailyBoardReports') ,(select id FROM eg_feature WHERE name = 'Daily Board Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DailyBoardReportResult') ,(select id FROM eg_feature WHERE name = 'Daily Board Reports'));

---Reports Between Due Dates
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LCMSDUEReports') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CaDueReportform') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CaDueReportResult') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Report_PWR_Due') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PwrDueReportResult') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CaDueReportResult') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'judgementImplDueReportResult') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'judgementImplDueReport') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'employeeHearingDueReport') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'employeehearingDueReportResult') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));


------------------------------------ADDING FEATURE ROLE STARTS--------------------------

---Time Series Report
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Time Series Report'));

---Generic Sub Reports
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Generic Sub Reports'));

---Daily Board Reports
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Daily Board Reports'));

---Reports Between Due Dates
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Reports Between Due Dates'));

----------------------------------------End----------------------------------------------------
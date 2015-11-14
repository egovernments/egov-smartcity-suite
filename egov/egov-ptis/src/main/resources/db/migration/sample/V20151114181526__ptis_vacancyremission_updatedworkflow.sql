delete from eg_wf_matrix where objecttype = 'VacancyRemission';



INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', '', 'VacancyRemission' || ':' ||'Assistant Approved', 'Commissioner Approval Pending', 'Commissioner', 'Assistant Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Assistant Approved', NULL, NULL, 'Commissioner', '', 'VacancyRemission' || ':' ||'Commissioner Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Commissioner Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Commissioner Approved', NULL, NULL, 'Bill Collector', '', 'VacancyRemission' || ':' ||'Bill Collector Approved', 'UD Revenue Inspector Approval Pending', 'UD Revenue Inspector', 'Bill Collector Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Bill Collector Approved', NULL, NULL, 'UD Revenue Inspector', '', 'VacancyRemission' || ':' ||'UD Revenue Inspector Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' ||'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', '', 'VacancyRemission' || ':' ||'END', 'END', NULL, NULL, 'Generate Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (NEXTVAL('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'Created', NULL, NULL, 'NULL', '', 'VacancyRemission' || ':' ||'NEW', 'Assistant approval pending', 'Senior Assistant,Junior Assistant', 'Assistant Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');


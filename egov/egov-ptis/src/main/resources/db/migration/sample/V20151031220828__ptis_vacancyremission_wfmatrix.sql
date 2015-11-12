
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'NEW', NULL, NULL, 'Revenue Clerk', '', 'VacancyRemission' || ':' ||'Revenue Clerk Approved', 'Commissioner Approval Pending', 'Commissioner', 'Revenue Clerk Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Revenue Clerk Approved', NULL, NULL, 'Commissioner', '', 'VacancyRemission' || ':' ||'Commissioner Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Commissioner Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Commissioner Approved', NULL, NULL, 'Bill Collector', '', 'VacancyRemission' || ':' ||'Bill Collector Approved', 'UD Revenue Inspector Approval Pending', 'UD Revenue Inspector', 'Bill Collector Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' || 'Bill Collector Approved', NULL, NULL, 'UD Revenue Inspector', '', 'VacancyRemission' || ':' ||'UD Revenue Inspector Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'VacancyRemission', 'VacancyRemission' || ':' ||'Rejected', NULL, NULL, 'Revenue Clerk', '', 'VacancyRemission' || ':' ||'END', 'END', NULL, NULL, 'Generate Notice', NULL, NULL, '2015-04-01', '2099-04-01');




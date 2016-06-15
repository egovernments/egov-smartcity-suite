	--------------------------Collection Summary--------------------------
		-----------------START--------------------
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='CollectionSummaryReport'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='CollectionSummaryReportResult'));
		------------------END---------------------

	--------------------------Remittance voucher report--------------------------
		-----------------START--------------------
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ERP Report Viewer'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='ERP Report Viewer'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='AjaxAccountListOfService'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='ServiceListOfAccount'));
		------------------END---------------------

	------------------------Receipt register report------------------------
		-----------------START--------------------
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='ReceiptRegisterReport'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='ReceiptRegisterReportResult'));
		------------------END---------------------





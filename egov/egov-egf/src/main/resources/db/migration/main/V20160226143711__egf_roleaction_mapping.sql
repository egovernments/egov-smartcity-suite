Insert into eg_roleaction   values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Remittance recovery view'));
Insert into eg_roleaction   values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajax-common-loadaccnumber'));
Insert into eg_roleaction   values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-voucherPDFprint'));
Insert into eg_roleaction   values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-voucherXLSprint'));

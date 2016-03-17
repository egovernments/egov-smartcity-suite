Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetLoadBeforeUpload'));

Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetLoadUpload'));

Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetLoadExport'));



Insert into eg_roleaction values((select id from eg_role where name='Budget Approver'),(select id from eg_action where name='Search Approve Uploaded Budget'));

Insert into eg_roleaction values((select id from eg_role where name='Budget Approver'),(select id from eg_action where name='Update Approve Uploaded Budget'));



Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Search BudgetUploadReport'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Search Result-BudgetUploadReport'));


Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BudgetUploadReport-getReferenceBudget'));

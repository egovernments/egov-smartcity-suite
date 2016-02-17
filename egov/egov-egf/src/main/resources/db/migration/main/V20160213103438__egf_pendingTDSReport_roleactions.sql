Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='TDS Summary'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='TDS Summary'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Pending TDS'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Pending TDS'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax pendingTDSReport'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax pendingTDSReport'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Report-pendingTDSReport'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Report-pendingTDSReport'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Report-pendingTDS_Pdf'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Report-pendingTDS_Pdf'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Report-pendingTDS_Xls'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Report-pendingTDS_Xls'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Report-TDSexportSummaryPdf'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Report-TDSexportSummaryPdf'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Report-TDSexportSummaryXls'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Report-TDSexportSummaryXls'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-report-tds-loadEntity'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-report-tds-loadEntity'));

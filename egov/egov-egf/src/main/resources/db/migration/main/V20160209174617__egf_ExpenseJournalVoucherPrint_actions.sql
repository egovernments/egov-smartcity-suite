Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ExpenseJournalVoucherPrintPDF','/report/expenseJournalVoucherPrint-exportPdf.action',null,(select id from eg_module where name='EGF-COMMON'),1,'ExpenseJournalVoucherPrintPDF',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseJournalVoucherPrintPDF'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ExpenseJournalVoucherPrintPDF'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ExpenseJournalVoucherPrintPDF'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ExpenseJournalVoucherPrintXLS','/report/expenseJournalVoucherPrint-exportXls.action',null,(select id from eg_module where name='EGF-COMMON'),1,'ExpenseJournalVoucherPrintXLS',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseJournalVoucherPrintXLS'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ExpenseJournalVoucherPrintXLS'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ExpenseJournalVoucherPrintXLS'));

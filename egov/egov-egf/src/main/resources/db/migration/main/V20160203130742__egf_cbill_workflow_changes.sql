update eg_wf_types set link = '/EGF/bill/contingentBill-beforeView.action?billRegisterId=:ID&mode=approve' , type = 'EgBillregister' ,displayname = 'Expense Bill'  where type = 'Cbill';

-- Workflow designation and approver dropdown roleaction
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Bill Creator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Bill Creator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Bill Approver'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Bill Approver'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ContingentBillUpdate','/bill/contingentBill-update.action',null,(select id from eg_module where name='EGF-COMMON'),1,'ContingentBillUpdate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ContingentBillUpdate'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ContingentBillUpdate'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ContingentBillUpdate'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='Expense Bill-Create'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ajax-common-codesofdetailtype'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='AjaxMiscReceiptSubScheme'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='AjaxMiscReceiptFundSource'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='AjaxMiscReceiptScheme'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ajax-common-checklist'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='searchEntries-accountdetail'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ExpenseBillCreate'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='Contingent Bill-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='cbill-print'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='cbill-print'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='cbill-ajaxprint'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='cbill-ajaxprint'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='View Bill Registers-Search'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='BillRegisterSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='Bill View-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='View Bill Registers-Search'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='BillRegisterSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='Bill View-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='Contingent Bill-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='View Bill Registers-Search'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='BillRegisterSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Bill View-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='cbill-print'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='cbill-ajaxprint'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='View Bill Registers-Search'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='BillRegisterSearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bill View-View'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='cbill-print'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='cbill-ajaxprint'));



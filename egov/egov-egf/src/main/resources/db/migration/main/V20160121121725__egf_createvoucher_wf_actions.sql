update eg_wf_types set link = '/EGF/voucher/preApprovedVoucher-loadvoucher.action?vhid=:ID&from=Journal Voucher' where type = 'CVoucherHeader';

-- Workflow designation and approver dropdown roleaction
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Voucher Creator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Voucher Creator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Voucher Approver'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Voucher Approver'));




Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'PreApprovedVoucherUpdate','/voucher/preApprovedVoucher-update.action',null,(select id from eg_module where name='EGF-COMMON'),1,'PreApprovedVoucherUpdate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='PreApprovedVoucherUpdate'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='PreApprovedVoucherUpdate'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='PreApprovedVoucherUpdate'));





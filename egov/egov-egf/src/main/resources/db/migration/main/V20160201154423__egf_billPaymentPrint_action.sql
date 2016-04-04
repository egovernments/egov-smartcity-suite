

insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
values(nextval('seq_eg_action'),'payment-voucherPDFprint','/report/billPaymentVoucherPrint-exportPdf.action',null,
(select id from eg_module where name='EGF-COMMON'),null,null,false,'EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='payment-voucherPDFprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-voucherPDFprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-voucherPDFprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='payment-voucherPDFprint'));





insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
values(nextval('seq_eg_action'),'payment-voucherXLSprint','/report/billPaymentVoucherPrint-exportXls.action',null,
(select id from eg_module where name='EGF-COMMON'),null,null,false,'EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='payment-voucherXLSprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-voucherXLSprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-voucherXLSprint'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='payment-voucherXLSprint'));


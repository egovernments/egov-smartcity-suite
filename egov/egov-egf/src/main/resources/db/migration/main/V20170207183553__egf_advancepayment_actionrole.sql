----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Advance Payment', true, 'egf', (select id from eg_module where name = 'Payments'), 'Advance Payment', 2);
----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Advance Requisition for Payment','/advancepayment/search',(select id from eg_module where name='Advance Payment' ),1,'Advance Payment',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Advance Requisition for Payment'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Advance Requisition for Payment'));
----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'ajax Advance Requisition for Payment','/common/ajaxarfnumbers-searcharf',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ajax Advance Requisition for Payment'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax Advance Requisition for Payment'));

---

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'get party type by arfnumber','/common/getpartytypebyarfnumber',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='get party type by arfnumber'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='get party type by arfnumber'));

-----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Get Advance Bills','/advancepayment/ajaxsearch',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Get Advance Bills'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Get Advance Bills'));

----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Advance Payment newform','/advancepayment/newform',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Advance Payment newform'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Advance Payment newform'));

---------

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Accountno by branchid for payment','/common/getaccountnobybranchidforpayment',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Accountno by branchid for payment'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Accountno by branchid for payment'));

-----

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'get Account Balance','/common/getaccountbalance',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='get Account Balance'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='get Account Balance'));

---
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Advance Payment create','/advancepayment/create',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Advance Payment create'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Advance Payment create'));

----

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Advance Payment success','/advancepayment/success',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Advance Payment success'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Advance Payment success'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Advance Payment success'));

--
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Advance Payment view','/advancepayment/view',(select id from eg_module where name='Advance Payment' ),null,null,false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Advance Payment view'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Advance Payment view'));

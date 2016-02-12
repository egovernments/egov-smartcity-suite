Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemitRecoverySearch','/deduction/remitRecovery-search.action',null,(select id from eg_module where name='Remittance Recovery'),1,'RemitRecoverySearch',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='RemitRecoverySearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RemitRecoverySearch'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Remittance Recovery-Create'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemitRecoveryRemit','/deduction/remitRecovery-remit.action',null,(select id from eg_module where name='Remittance Recovery'),1,'RemitRecoveryRemit',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='RemitRecoveryRemit'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RemitRecoveryRemit'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemitRecoverySendForApproval','/deduction/remitRecovery-sendForApproval.action',null,(select id from eg_module where name='Remittance Recovery'),1,'RemitRecoverySendForApproval',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='RemitRecoverySendForApproval'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RemitRecoverySendForApproval'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='RemitRecoverySendForApproval'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RemitRecoveryCreate','/deduction/remitRecovery-create.action',null,(select id from eg_module where name='Remittance Recovery'),1,'RemitRecoveryCreate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='RemitRecoveryCreate'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RemitRecoveryCreate'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Remittancerecovry-inbox'), (select id from eg_role where name = 'Payment Creator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Remittancerecovry-inbox'), (select id from eg_role where name = 'Payment Approver'));


-- Workflow designation and approver dropdown roleaction
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Payment Creator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Payment Creator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'Payment Approver'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'Payment Approver'));



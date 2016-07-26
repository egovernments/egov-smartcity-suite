-----------------Creating Works Administrator Menu tree----------------
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksAdministrator','true',null,(select id from eg_module where name = 'Works Management'),'Works Administrator', 1);

-----------------Role action mappings to search Contractor bills form----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchContractorBillToCancelForm','/contractorbill/cancel/search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Cancel Contractor Bill','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchContractorBillToCancelForm' and contextroot = 'egworks'));

-----------------Role action mappings to search Contractor bills----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchContractorBillsToCancel','/contractorbill/cancel/ajax-search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search Contractor Bills to Cancel','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxSearchContractorBillsToCancel' and contextroot = 'egworks'));

-----------------Role action mappings to search Work Id Numbers for Cancelling Contractor bills----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxWorkIdentificationNumbersToCancelContractorBill','/contractorbill/ajaxworkidentificationnumbers-contractorbilltocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Ajax Work Identification Numbers To Cancel ContractorBill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxWorkIdentificationNumbersToCancelContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to search Bill Numbers for Cancelling Contractor bills----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxBillNumbersToCancelContractorBill','/contractorbill/ajaxbillnumbers-contractorbilltocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Ajax Bill Numbers To Cancel ContractorBill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxBillNumbersToCancelContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to get Contractor bill screen to cancel----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CancelContractorBill','/contractorbill/cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Cancel Contractor Bill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'CancelContractorBill' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='CancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'CancelContractorBill' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxBillNumbersToCancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxBillNumbersToCancelContractorBill' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkIdentificationNumbersToCancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxWorkIdentificationNumbersToCancelContractorBill' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchContractorBillsToCancel' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchContractorBillsToCancel' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchContractorBillToCancelForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchContractorBillToCancelForm' and contextroot = 'egworks';

--rollback delete from eg_module where name = 'WorksAdministrator' and PARENTMODULE = (select id from eg_module where name = 'Works Management');
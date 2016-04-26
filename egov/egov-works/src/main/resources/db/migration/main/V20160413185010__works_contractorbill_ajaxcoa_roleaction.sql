------------------ Role Action mappings to update Contractor Bill Register --------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAjaxDeductionCOAForContractorBill','/contractorbill/ajaxdeduction-coa',null,(select id from EG_MODULE where name = 'WorksContractorBill'),1,'WorksAjaxDeductionCOAForContractorBill',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='WorksAjaxDeductionCOAForContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='WorksAjaxDeductionCOAForContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='WorksAjaxDeductionCOAForContractorBill' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid in (select id from eg_role where name in ('Super User','Works Creator','Works Approver')) and actionid in (select id from eg_action where name in('WorksAjaxDeductionCOAForContractorBill') and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksAjaxDeductionCOAForContractorBill' and contextroot = 'egworks';



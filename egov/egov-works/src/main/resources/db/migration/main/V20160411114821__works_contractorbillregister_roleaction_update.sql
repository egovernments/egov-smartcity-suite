------------------ Role Action mappings to update Contractor Bill Register --------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksUpdateContractorBillRegister','/contractorbill/update',null,(select id from EG_MODULE where name = 'WorksContractorBill'),1,'WorksUpdateContractorBillRegister',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='WorksUpdateContractorBillRegister'));
Insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='WorksUpdateContractorBillRegister'));
Insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='WorksUpdateContractorBillRegister'));

insert into eg_roleaction values((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='WorksSaveContractorBillSuccess' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Approver') and actionid = (select id from eg_action where name ='WorksSaveContractorBillSuccess' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Approver') and actionid = (select id from eg_action where name ='WorksUpdateContractorBillRegister' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Creator') and actionid = (select id from eg_action where name ='WorksUpdateContractorBillRegister' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksUpdateContractorBillRegister' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksUpdateContractorBillRegister' and contextroot = 'egworks';



insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'ContractCertificatePDF','/contractcertificate/contractcertificatePDF',null,(select id from eg_module where name='WorksContractorBill'),5,
'Contract Certificate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='ContractCertificatePDF'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='ContractCertificatePDF'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name = 'ContractCertificatePDF') and roleid in(select id from eg_role where name in('Super User','Bill Creator','Bill Approver','Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Works Creator','Works Approver'));
--rollback delete from eg_action where name='ContractCertificatePDF' and contextroot='egworks';
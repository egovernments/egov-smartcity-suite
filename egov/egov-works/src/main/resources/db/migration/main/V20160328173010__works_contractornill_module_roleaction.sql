insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksContractorBill','true',null,(select id from eg_module where name = 'Works Management'),'Contractor Bill', 7);

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateContractorBillNewForm','/contractorbill/newform',null,(select id from EG_MODULE where name = 'WorksContractorBill'),1,'WorksCreateContractorBillNewForm',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateContractorBillNewForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksCreateContractorBillNewForm' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksCreateContractorBillNewForm') and contextroot = 'egworks') and roleid in(select id from eg_role where name in('Super User','Works Creator'));
--rollback delete from eg_action where name in ('WorksCreateContractorBillNewForm') and contextroot = 'egworks';
--rollback delete from eg_module where name in ('WorksContractorBill');
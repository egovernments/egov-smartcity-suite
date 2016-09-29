---Refund Role action mapping
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'getTotalDebitCreditAmountByAccountCode','/contractorbill/gettotalcreditanddebitamount',null,(select id from EG_MODULE where name = 'WorksContractorBill'),0,'Total Credit and Debit Amount by Account code','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'getTotalDebitCreditAmountByAccountCode' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'getTotalDebitCreditAmountByAccountCode' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'getTotalDebitCreditAmountByAccountCode' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='getTotalDebitCreditAmountByAccountCode') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='getTotalDebitCreditAmountByAccountCode' and contextroot='egworks';
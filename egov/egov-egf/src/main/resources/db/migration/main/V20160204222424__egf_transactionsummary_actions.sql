update eg_action set url = '/transactionsummary/ajax/searchTransactionSummariesForNonSubledger' , name = 'SearchTransactionSummariesForNonSubledger' where name = 'Ajax Search';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'SearchTransactionSummariesForSubledger','/transactionsummary/ajax/searchTransactionSummariesForSubledger',null,309,null,'SearchTransactionSummariesForSubledger','false','EGF',0,1,current_date,1,
current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'SearchTransactionSummariesForSubledger'));


insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Financial Administrator'), (select id from eg_action where name = 'SearchTransactionSummariesForSubledger'));



CREATE UNIQUE INDEX transactionsummary_unique_check ON transactionsummary (glcodeid, accountdetailtypeid,accountdetailkey,financialyearid,fundid,functionid,departmentid);

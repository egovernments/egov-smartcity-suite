

delete from eg_roleaction where actionid in (select id from eg_action where url like '%/transactionsummary/%' );
delete from eg_action where url like '%/transactionsummary/%';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Opening Balance Entry','/transactionsummary/new',null,309,null,'Opening Balance Entry','true','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Opening Balance Entry'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Major Head Ajax','/transactionsummary/ajax/getMajorHeads',null,309,null,'Ajax Major Heads','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Major Head Ajax'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Minor Head Ajax','/transactionsummary/ajax/getMinorHeads',null,309,null,'Ajax Minor Heads','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Minor Head Ajax'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Ajax Accounts','/transactionsummary/ajax/getAccounts',null,309,null,'Ajax Accounts','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Ajax Accounts'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Ajax Transaction Submit','/transactionsummary/create',null,309,null,'Ajax Submit','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Ajax Transaction Submit'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Ajax Search','/transactionsummary/ajax/searchTransactionSummaries',null,309,null,'Ajax Search','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Ajax Search'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Ajax Delete','/transactionsummary/ajax/deleteTransaction',null,309,null,'Ajax Delete','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Ajax Delete'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Ajax get TransactionSummary','/transactionsummary/ajax/getTransactionSummary',null,309,null,'Ajax get TransactionSummary','false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name = 'Ajax get TransactionSummary'));

update financialyear set lastmodified =now();
update financialyear set created =now();
alter table transactionsummary add column version numeric;
 ALTER TABLE TransactionSummary  ADD COLUMN createdby bigint;
 ALTER TABLE TransactionSummary  ADD COLUMN createdDate timestamp;

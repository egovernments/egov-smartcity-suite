Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'SearchNoticeSearchResult','/reports/searchNotices-search', null, (select id from eg_module where name = 'PTIS-Reports'), null, 'SearchNoticeSearchResult',
 false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Property Tax'));

Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'SearchNoticeMergeAndDownload','/reports/searchNotices-mergeAndDownload', null, (select id from eg_module where name = 'PTIS-Reports'), null, 'SearchNoticeMergeAndDownload',
 false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Property Tax'));

Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'SearchNoticeZipAndDownload','/reports/searchNotices-zipAndDownload', null, (select id from eg_module where name = 'PTIS-Reports'), null, 'SearchNoticeZipAndDownload',
 false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Property Tax'));
 
Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'SearchNoticeReset','/reports/searchNotices-reset', null, (select id from eg_module where name = 'PTIS-Reports'), null, 'SearchNoticeReset',
 false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Property Tax'));


Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='SearchNoticeSearchResult'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Approver'), (select id from eg_action where name='SearchNoticeSearchResult'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='SearchNoticeSearchResult'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Administrator'), (select id from eg_action where name='SearchNoticeSearchResult'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='SearchNoticeMergeAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Approver'), (select id from eg_action where name='SearchNoticeMergeAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='SearchNoticeMergeAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Administrator'), (select id from eg_action where name='SearchNoticeMergeAndDownload'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='SearchNoticeZipAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Approver'), (select id from eg_action where name='SearchNoticeZipAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='SearchNoticeZipAndDownload'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Administrator'), (select id from eg_action where name='SearchNoticeZipAndDownload'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='SearchNoticeReset'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Approver'), (select id from eg_action where name='SearchNoticeReset'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='SearchNoticeReset'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Administrator'), (select id from eg_action where name='SearchNoticeReset'));

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Administrator') from eg_action where name in ('Zone Wise Collection Report',
'Ward Wise Collection Report',
'Block Wise Collection Report',
'Locality Wise Collection Report',
'Search Notice');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Administrator') from eg_action where name in ('Search Property',
'Search Property By Index',
'Search Property By Bndry',
'Search Property By Area',
'Search Property By Location',
'Search Property By Demand',
'Search Property By Assessment');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Administrator') from eg_action where name in ('View Property');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Administrator') from eg_action where name in ('Populate Wards');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Administrator') from eg_action where name in ('View DCB Property Display');

--rollback delete from eg_roleaction  where actionid = (select id from eg_action where name='SearchNoticeSearchResult') and roleid in (select id from eg_role where name in ('ULB Operator', 'Property Approver', 'Property Verifier'));
--rollback delete from eg_action where name = 'SearchNoticeSearchResult';
--rollback delete from eg_roleaction  where actionid = (select id from eg_action where name='SearchNoticeMergeAndDownload') and roleid in (select id from eg_role where name in ('ULB Operator', 'Property Approver', 'Property Verifier'));
--rollback delete from eg_action where name = 'SearchNoticeMergeAndDownload';
--rollback delete from eg_roleaction  where actionid = (select id from eg_action where name='SearchNoticeZipAndDownload') and roleid in (select id from eg_role where name in ('ULB Operator', 'Property Approver', 'Property Verifier'));
--rollback delete from eg_action where name = 'SearchNoticeZipAndDownload';
--rollback delete from eg_roleaction  where actionid = (select id from eg_action where name='SearchNoticeReset') and roleid in (select id from eg_role where name in ('ULB Operator', 'Property Approver', 'Property Verifier'));
--rollback delete from eg_action where name = 'SearchNoticeReset';
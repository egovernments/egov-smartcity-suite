Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'SearchNoticeShowNotice','/reports/searchNotices-showNotice', null, (select id from eg_module where name = 'PTIS-Reports'), null, 'SearchNoticeShowNotice',
 false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Property Tax'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='SearchNoticeShowNotice'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Approver'), (select id from eg_action where name='SearchNoticeShowNotice'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='SearchNoticeShowNotice'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Administrator'), (select id from eg_action where name='SearchNoticeShowNotice'));

--roleback delete from eg_roleaction  where roleid in (select id from eg_role where name in ('ULB Operator', 'Property Approver', 'Property Verifier', 'Property Administrator')) and actionid in (select id from eg_action where name='SearchNoticeShowNotice');
--roleback delete from eg_action where name = 'SearchNoticeShowNotice';
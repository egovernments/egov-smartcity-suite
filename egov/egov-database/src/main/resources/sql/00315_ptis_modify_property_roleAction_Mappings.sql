Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Forward Modify Property','/modify/modifyProperty-forward.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Forward Modify Property', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'View Modify Property','/modify/modifyProperty-view.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'View Modify Property', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Forward View Modify Property','/modify/modifyProperty-forwardView.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Forward View Modify Property', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Approve Modify Property','/modify/modifyProperty-approve.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Approve Modify Property', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Reject Modify Property','/modify/modifyProperty-reject.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Reject Modify Property', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);


insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Forward Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='View Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Forward View Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='Approve Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='Forward View Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='View Modify Property'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Property Verifier'), (select id from eg_action where name='Reject Modify Property'));

--delete from eg_roleaction where roleid in (select id from eg_role where name='Property Verifier') and actionid in (select id from eg_action where name='Reject Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='Property Verifier') and actionid in (select id from eg_action where name='Approve Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='Property Verifier') and actionid in (select id from eg_action where name='View Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='Property Verifier') and actionid in (select id from eg_action where name='Forward Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='CSC Operator') and actionid in (select id from eg_action where name='View Modify Property');
--delete from eg_roleaction where roleid in (select id from eg_role where name='CSC Operator') and actionid in (select id from eg_action where name='Forward Modify Property');

--delete from eg_action where name = 'View Modify Property';
--delete from eg_action where name = 'Forward Modify Property';
--delete from eg_action where name = 'Forward View Modify Property';
--delete from eg_action where name = 'Approve Modify Property';
--delete from eg_action where name = 'Reject Modify Property';
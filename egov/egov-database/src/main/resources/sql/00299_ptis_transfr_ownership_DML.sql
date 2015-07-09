update eg_action set url='/transfer/property-new.action' where url = '/transfer/transferProperty-transferForm.action';
update eg_action set url='/transfer/property-save.action' where url = '/transfer/transferProperty.action';

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property View','/transfer/property-view.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property View', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property View'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property Approve','/transfer/property-approve.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property Approve', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property Approve'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property Forward','/transfer/property-forward.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property Forward', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property Forward'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property Reject','/transfer/property-reject.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property Reject', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property Reject'));

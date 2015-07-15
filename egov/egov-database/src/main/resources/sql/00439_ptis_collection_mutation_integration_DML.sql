INSERT INTO eg_module VALUES (nextval('seq_eg_module'), 'Billbased Services', true, null, (select id from eg_module where name='Collection Transaction'), 'Billbased Services', 1);

insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate,application) 
values (nextval('seq_eg_action'),'Mutation Fee Payment Generate','/property/transfer/collect-fee.action', null, (select ID from eg_module where "name" ='Existing property'), null, 'Mutation Fee Payment Generate', false, 'ptis', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where "name" ='Property Tax'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where "name"='Mutation Fee Payment Generate'));

insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate,application) 
values (nextval('seq_eg_action'),'Mutation Fee Payment','/../ptis/property/transfer/search.action', null, (select ID from eg_module where "name" ='Billbased Services'), null, 'Property Mutation Fee', true, 'collection', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where "name" ='Collection'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where "name"='Mutation Fee Payment'));

insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate,application) 
values (nextval('seq_eg_action'),'Mutation Fee Payment Search','/property/transfer/search.action', null, (select ID from eg_module where "name" ='Existing property'), null, 'Property Mutation Fee Search', false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where "name" ='Property Tax'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where "name"='Mutation Fee Payment Search'));

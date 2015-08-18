insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate,application) 
values (nextval('seq_eg_action'),'Property Tax Collection','/../ptis/search/searchProperty-searchForm.action', null, (select ID from eg_module where "name" ='Billbased Services'), null, 'Property Tax', true, 'collection', null, 1, current_timestamp,1,current_timestamp, (select id from eg_module where "name" ='Collection'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where "name"='Property Tax Collection' and displayname='Property Tax'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where "name"='View DCB Property Display' and displayname='View DCB Property'));

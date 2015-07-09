Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transferee Delete','/property/transfer/delete-transferee.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transferee Delete', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transferee Delete'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='Transferee Delete'));

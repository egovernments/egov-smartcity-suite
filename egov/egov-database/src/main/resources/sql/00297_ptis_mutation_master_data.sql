Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Modify Property Action','/modify/modifyProperty-save.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Modify Property Action', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Modify Property Action'));

insert into egpt_mutation_master(id, mutation_name, mutation_desc, type, code, order_id) 
values (nextval('seq_egpt_mutation_master'), 'ADDITION OR ALTERATION', 'Addition or Alteration', 'MODIFY', 'ADD OR ALTER', (SELECT MAX(ORDER_ID) + 1 FROM EGPT_MUTATION_MASTER WHERE TYPE = 'MODIFY'));

insert into egpt_mutation_master(id, mutation_name, mutation_desc, type, code, order_id) 
values (nextval('seq_egpt_mutation_master'), 'PART DEMOLITION', 'Part Demolition', 'MODIFY', 'PART DEMOLITION', (SELECT MAX(ORDER_ID) + 1 FROM EGPT_MUTATION_MASTER WHERE TYPE = 'MODIFY'));


--rollback DELETE FROM EGPT_MUTATION_MASTER WHERE MUTATION_NAME IN ('ADDITION OR ALTERATION', 'PART DEMOLITION') AND TYPE = 'MODIFY';

--rollback delete from eg_roleaction where roleid in (select id from eg_role where name='Data Entry Operator') and actionid in (select id from eg_action where name='Modify Property Action');
--rollback delete from eg_action where name = 'Modify Property Action';

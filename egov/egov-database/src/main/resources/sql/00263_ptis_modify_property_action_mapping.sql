update eg_action set url = '/modify/modifyProperty-ownerForm.action' where name = 'Edit Owner';
update eg_action set url = '/modify/modifyProperty-updateOwner.action' where name = 'Edit Owner submit';

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Modify Property Form','/modify/modifyProperty-modifyForm.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Modify Property Form', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Modify Property Form'));

--rollback delete from eg_roleaction where roleid in (select id from eg_role where name='Data Entry Operator') and actionid in (select id from eg_action where name='Modify Property Form');
--rollback delete from eg_action where name = 'Modify Property Form';

--rollback update eg_action set url = '/modify/modifyProperty-/modify/modifyProperty-modifyForm.action' where name = 'Edit Owner';
--rollback update eg_action set url = '/modify/modifyProperty-/modify/modifyProperty-updateOwner.action' where name = 'Edit Owner submit';

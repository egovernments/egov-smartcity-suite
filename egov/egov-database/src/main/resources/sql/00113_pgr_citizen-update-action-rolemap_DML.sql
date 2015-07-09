

delete from eg_roleaction_map where actionid in (select id  from eg_action where url like '%/complaint-update%');
delete from eg_action where url like '%/complaint-update%';



INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Citizen Update Complaint', null, null, now(), '/complaint/update/', null, 
null,(Select id_module from eg_module where module_name='PGR'), null, 'Citizen Update Complaint', 0, null, 'pgr');

--rollback delete from eg_action where url='/complaint/update/';

--insert into eg_roleaction_map (Actionid,roleid)
--values((select id from eg_action where url='/complaint/update/'),(Select id from eg_role where name='Citizen'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/complaint/update/citizen/');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/complaint/update/'),(Select id from eg_role where name='Super User'));


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/complaint/update/'),(Select id from eg_role where name='Grievance Officer'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/complaint/update/'),(Select id from eg_role where name='Citizen'));


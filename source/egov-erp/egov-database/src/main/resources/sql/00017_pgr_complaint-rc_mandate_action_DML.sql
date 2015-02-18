INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'RCCRNRequiredOfficials', null, null, now(), '/complaint/officials/isCrnRequired', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'RCCRNRequiredOfficials', 0, null, 'pgr');

--rollback delete from eg_action where name='RCCRNRequiredOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RCCRNRequiredOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RCCRNRequiredOfficials' and context_root='pgr');
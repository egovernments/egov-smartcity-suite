INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'load Designations', null, null, now(), '/ajax-approvalDesignations', 'approvalDepartment=', 
null,(Select id_module from eg_module where module_name='PGR'), null, 'load Designations', 0, null, 'pgr');



--rollback delete from eg_action where url='/ajax-approvalDesignations';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-approvalDesignations'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-approvalDesignations'),(Select id from eg_role where name='PGR_Officer'));


--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/ajax-approvalDesignations');



INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'load Positions', null, null, now(), '/ajax-approvalPositions', 'approvalDepartment=', 
null,(Select id_module from eg_module where module_name='PGR'), null, 'load Positions', 0, null, 'pgr');



--rollback delete from eg_action where url='/ajax-approvalPositions';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-approvalPositions'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-approvalPositions'),(Select id from eg_role where name='PGR_Officer'));

----rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/ajax-approvalPositions');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'load Wards', null, null, now(), '/ajax-getWards', 'id=', 
null,(Select id_module from eg_module where module_name='PGR'), null, 'load Wards', 0, null, 'pgr');



--rollback delete from eg_action where url='/ajax-getWards';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-getWards'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/ajax-getWards'),(Select id from eg_role where name='PGR_Officer'));

update eg_boundary set version=0;
update eg_boundary_type set version=0;
update eg_hierarchy_type set version=0;


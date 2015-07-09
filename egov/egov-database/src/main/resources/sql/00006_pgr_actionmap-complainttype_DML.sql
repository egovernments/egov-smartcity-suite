INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num)
 VALUES (nextval('SEQ_MODULEMASTER'), 'Pgr Masters', now(), 1, 'Masters', null, (Select id_module from eg_module where module_name='PGR' and parentid is null), 
 'Masters', 2);
--rollback delete from eg_module where module_name = 'Pgr Masters';



INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Add Complaint Type', null, null, now(), '/complaint-type', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Create Complaint Type', 1, null, 'pgr');

--rollback delete from eg_action where url='/complaint-type';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/complaint-type'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/complaint-type');


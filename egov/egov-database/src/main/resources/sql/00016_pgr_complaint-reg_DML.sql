INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num)
 VALUES (nextval('SEQ_MODULEMASTER'), 'PGRComplaints', now(), 1, 'Complaint', null, (Select id_module from eg_module where module_name='PGR' and parentid is null), 
 'Complaint', 2);
 
--rollback delete from eg_module where module_name = 'PGRComplaints';

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintRegisteration', null, null, now(), '/complaint/citizen/show-reg-form', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'Register Complaint', 1, null, 'pgr');

--rollback delete from eg_action where name='ComplaintRegisteration' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintRegisteration'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintRegisteration' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintTypeAjax', null, null, now(), '/complaint/citizen/complaintTypes', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintTypeAjax', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintTypeAjax' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintTypeAjax'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintTypeAjax' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintSave', null, null, now(), '/complaint/citizen/register', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintSave', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintSave' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintSave'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintSave' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintLocationRequired', null, null, now(), '/complaint/citizen/isLocationRequired', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintLocationRequired', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintLocationRequired' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationRequired'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationRequired' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintLocations', null, null, now(), '/complaint/citizen/locations', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintLocations', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintLocations' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocations'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocations' and context_root='pgr');




INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintRegisterationOfficials', null, null, now(), '/complaint/officials/show-reg-form', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'Officials Register Complaint', 1, null, 'pgr');

--rollback delete from eg_action where name='ComplaintRegisterationOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintRegisterationOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintRegisterationOfficials' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintTypeAjaxOfficials', null, null, now(), '/complaint/officials/complaintTypes', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintTypeAjaxOfficials', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintTypeAjaxOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintTypeAjaxOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintTypeAjaxOfficials' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintSaveOfficials', null, null, now(), '/complaint/officials/register', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintSaveOfficials', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintSaveOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintSaveOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintSaveOfficials' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintLocationRequiredOfficials', null, null, now(), '/complaint/officials/isLocationRequired', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintLocationRequiredOfficials', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintLocationRequiredOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationRequiredOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationRequiredOfficials' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'ComplaintLocationsOfficials', null, null, now(), '/complaint/officials/locations', null, 
null,(Select id_module from eg_module where module_name='PGRComplaints'), null, 'ComplaintLocationsOfficials', 0, null, 'pgr');

--rollback delete from eg_action where name='ComplaintLocationsOfficials' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationsOfficials'),(Select id_role from eg_roles where role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationsOfficials' and context_root='pgr');


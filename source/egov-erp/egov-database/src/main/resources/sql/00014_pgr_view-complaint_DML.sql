INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, 
display_name, is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'View Complaint', null, null,
now(), '/view-complaint', 'complaintId=', null,(Select id_module FROM eg_module WHERE module_name='PGRComplaints'), null, 
'View Complaint', 0, null, 'pgr');
--rollback delete from eg_action where url='/view-complaint';


INSERT INTO eg_roleaction_map (Actionid,roleid)
VALUES((SELECT id FROM eg_action WHERE url='/view-complaint'),(Select id_role FROM eg_roles WHERE role_name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/view-complaint');


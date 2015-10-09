INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AjaxLoadBoundarys', '/boundaries-by-boundaryType', null,
     (select id from eg_module where name='Boundary Module'), null, 'AjaxLoadBoundarys', false,
     'egi', 0, 1, now(), 1, now(),(Select id from eg_module where name='Administration' and parentmodule is null));

--rollback delete from eg_action where name = 'AjaxLoadBoundarys';


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxLoadBoundarys' and contextroot='egi'),
(SELECT id FROM eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name = 'AjaxLoadBoundarys');


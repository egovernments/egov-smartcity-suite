INSERT INTO eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name ='complaint downloadfile'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'PendingGrievance', '/pending/grievance-list', null, (select id from eg_module where name='PGRComplaints'), 4, 'My pending grievance', true, 'pgr', 0, 1, now(), 1, now(),(select id from eg_module where name='PGR' and parentmodule is null));

--rollback delete from eg_action where name='PendingGrievance' and contextroot='pgr';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PendingGrievance'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='PendingGrievance' and contextroot='pgr');

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AjaxPendingGrievanceResult', '/pending/ajax-grievancelist', null, (select id from eg_module where name='PGRComplaints'), 4, 'AjaxPendingGrievanceResult', false, 'pgr', 0, 1, now(), 1, now(),(select id from eg_module where name='PGR' and parentmodule is null));

--rollback delete from eg_action where name='AjaxPendingGrievanceResult' and contextroot='pgr';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxPendingGrievanceResult'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='AjaxPendingGrievanceResult' and contextroot='pgr');


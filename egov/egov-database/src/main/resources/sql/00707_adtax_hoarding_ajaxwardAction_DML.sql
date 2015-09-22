
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AjaxChildBoundaries', '/wards-by-zone', null, (select id from eg_module where name='Boundary Module'), 1, 'Ajax load ChildBoundaries', false, 'egi', 0, 1, now(), 1, now(),(select id from eg_module where name='Administration' and parentmodule is null));

--rollback delete from eg_action where name='AjaxChildBoundaries' and contextroot='adtax';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxChildBoundaries'));

--rollback delete from eg_roleaction  where actionid=(select id from eg_action where name='AjaxChildBoundaries' and contextroot='egi');




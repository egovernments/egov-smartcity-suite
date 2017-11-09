---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Sewerage Tax Approver', 'Sewerage Tax Approver', now(), 1, 1, now(), 0);

---------------Map the Action urls to the role--------------------
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'UpdateSewerageApplicationDetails' and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'DownloadFile' and contextroot = 'stms'));


--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and actionid = (select id from eg_action where name = 'DownloadFile' and contextroot = 'stms');
--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and actionid = (select id from eg_action where name = 'UpdateSewerageApplicationDetails' and contextroot = 'stms');
--rollback delete from eg_role where name = 'Sewerage Tax Approver';
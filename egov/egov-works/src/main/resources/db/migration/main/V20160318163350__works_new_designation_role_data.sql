---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Works Creator', 'Works Creator', now(), 1, 1, now(), 0);
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Works Approver', 'Works Approver', now(), 1, 1, now(), 0);

---------------Map the Action urls to the role--------------------
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksCreateLineEstimateNewForm' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksGetSubScheme' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksCreateNewLineEstimate' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksLineEstimateSuccess' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksUpdateLineEstimateForm' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksUpdateLineEstimate' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'DownloadLineEstimateDoc' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'ajaxSearchWardLocation' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksGetSubTypeOfWork' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxApproverByDesignationAndDepartment' and contextroot = 'eis'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxDesignationsByDepartment' and contextroot = 'eis'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksGetSubScheme' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksLineEstimateSuccess' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksUpdateLineEstimateForm' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksUpdateLineEstimate' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'DownloadLineEstimateDoc' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'ajaxSearchWardLocation' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchLineEstimateForm' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'Search LineEstimate' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'Ajax Search Line Estimate Numbers' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'Ajax Search Admin Sanction Numbers' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchLineEstimatesToCreateLOA' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchLineEstimateForLOA' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'ajaxsearchloa' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksGetSubTypeOfWork' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'AjaxApproverByDesignationAndDepartment' and contextroot = 'eis'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'AjaxDesignationsByDepartment' and contextroot = 'eis'));


--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Creator');
--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Approver');
--rollback delete from eg_role where name = 'Works Approver';
--rollback delete from eg_role where name = 'Works Creator';
-----------Removing action mapping for Works Approver to create loa---------------
DELETE FROM eg_roleaction WHERE roleid = (select id from eg_role where name = 'Works Approver') AND actionid = (select id from eg_action where name = 'WorksSearchLineEstimatesToCreateLOA' and contextroot = 'egworks');

--rollback INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchLineEstimatesToCreateLOA' and contextroot = 'egworks'));
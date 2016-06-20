------Search and View Roleactions to Works Approver and Works Administrator---
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='SearchMBHeader' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='SearchMBHeader' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxSearchWorkOrderNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxSearchWorkOrderNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxSearchEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxSearchEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxSearchContractors' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxSearchContractors' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='SearchMBHeaderSubmit' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='SearchMBHeaderSubmit' and contextroot = 'egworks'));

---delete from eg_roleaction where actionid in(select id from eg_action where name in ('SearchMBHeader','AjaxSearchWorkOrderNumbers','AjaxSearchEstimateNumbers','AjaxSearchContractors','SearchMBHeaderSubmit')) and roleid in(select id from eg_role where name in('Works Approver','Works Administrator'));

-----------------Role action mappings to search Contractor bills form----------------
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchContractorBillToCancelForm' and contextroot = 'egworks'));

-----------------Role action mappings to search Contractor bills----------------
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'AjaxSearchContractorBillsToCancel' and contextroot = 'egworks'));

-----------------Role action mappings to search Work Id Numbers for Cancelling Contractor bills----------------
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'AjaxWorkIdentificationNumbersToCancelContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to search Bill Numbers for Cancelling Contractor bills----------------
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'AjaxBillNumbersToCancelContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to get Contractor bill screen to cancel----------------
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'CancelContractorBill' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxBillNumbersToCancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkIdentificationNumbersToCancelContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchContractorBillsToCancel' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchContractorBillToCancelForm' and contextroot = 'egworks');
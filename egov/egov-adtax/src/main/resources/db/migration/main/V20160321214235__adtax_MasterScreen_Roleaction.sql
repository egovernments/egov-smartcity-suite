
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='Search Agency Hoardings'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='Search Agency Hoardings'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Search Agency Hoardings'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='Search Agency Hoardings'));


INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Saved Revenue Inspectors'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Revenue Inspector'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search TPBO'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create TPBO'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Update Revenue Inspectors'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Tpbo Update'), id from eg_role where name in ('Advertisement Tax Admin');

INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'deactivate advertisement'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Status Change Of Records'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Active Records'), id from eg_role where name in ('Advertisement Tax Admin');
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Deactivate The Record'), id from eg_role where name in ('Advertisement Tax Admin');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'generateDemandAdvertisementTax'));

update eg_action set displayname='Advertisement Collection Report' where name='dcbReportSearch';
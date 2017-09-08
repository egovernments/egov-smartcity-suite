INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'SearchDonationCharges', '/reports/search-donation', null,(select id from eg_module where name='WaterTaxReports'), 1, 'View Donation Charges DCB Report', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='SearchDonationCharges'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='SearchDonationCharges'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewDonationCharges', '/reports/view-donation', null,(select id from eg_module where name='WaterTaxReports'), 2, 'View Donation Charges', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewDonationCharges'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='ViewDonationCharges'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Donation Charges DCB Report','View Donation Charges DCB Report',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchDonationCharges') ,(select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewDonationCharges') ,(select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Water Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report'));


--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report') and role = (select id from eg_role where name = 'Water Tax Report Viewer');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report') and action = (select id FROM eg_action  WHERE name = 'SearchDonationCharges');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Donation Charges DCB Report') and action = (select id FROM eg_action  WHERE name = 'ViewDonationCharges');
--rollback delete from eg_feature where name='View Donation Charges DCB Report' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='SearchDonationCharges') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewDonationCharges') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='SearchDonationCharges') and roleid = (select id from eg_role where name = 'Water Tax Report Viewer');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewDonationCharges') and roleid = (select id from eg_role where name = 'Water Tax Report Viewer');

--rollback delete from eg_action where name = 'SearchDonationCharges' and parentmodule=(select id from eg_module where name='WaterTaxReports');
--rollback delete from eg_action where name = 'ViewDonationCharges' and parentmodule=(select id from eg_module where name='WaterTaxReports');




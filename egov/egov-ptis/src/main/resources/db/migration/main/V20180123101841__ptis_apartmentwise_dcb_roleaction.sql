
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ApartmentDCBReport'),id from eg_role where name in ('Property Verifier','Property Approver','SYSTEM');



INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ApartmentDCBReportResult'),id from eg_role where name in ('Property Verifier','Property Approver','SYSTEM');

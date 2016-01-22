INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Unit rate'),id from eg_role where name in ('Property Administrator','Property Verifier','Super User','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Unit rate Submit'),id from eg_role where name in ('Property Administrator','Property Verifier','Super User','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Unit rate'),id from eg_role where name in ('Property Administrator','Property Verifier','Super User','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Update Unit rate'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Unit Rate Master'),id from eg_role where name in ('Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Usage Master'),id from eg_role where name in ('Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Usage Master List'),id from eg_role where name in ('Property Verifier','Property Approver','ULB Operator');

delete from eg_roleaction where actionid in(select id from eg_action where name='Rebate Master' and contextroot='ptis');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Rebate Master'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Floor Type'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Floor Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Floor Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Roof Type'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Roof Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Roof Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Wall Type'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Wall Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Wall Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Wood Type'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Wood Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Wood Type'),id from eg_role where name in ('Property Administrator','Property Verifier','Property Approver','ULB Operator');


INSERT INTO eg_demand_reason_master
(id, reasonmaster, category, isdebit, module, code, "order", create_date, modified_date, isdemand)
VALUES(nextval('seq_eg_demand_reason_master'), 'Penalty Waive Off', (select id from eg_reason_category where code='REBATE'), 'N', (select id from eg_module where name = 'Property Tax'), 'PENALTY_WAIVEOFF', 1, now(), now(), false);

INSERT INTO eg_demand_reason_master
(id, reasonmaster, category, isdebit, module, code, "order", create_date, modified_date, isdemand)
VALUES(nextval('seq_eg_demand_reason_master'), 'Exempted Amount', (select id from eg_reason_category where code='REBATE'), 'N', (select id from eg_module where name = 'Property Tax'), 'EXEMPTED', 1, now(), now(), false);

INSERT INTO eg_demand_reason_master
(id, reasonmaster, category, isdebit, module, code, "order", create_date, modified_date, isdemand)
VALUES(nextval('seq_eg_demand_reason_master'), 'Court Case Amount', (select id from eg_reason_category where code='WRITEOFF'), 'N', (select id from eg_module where name = 'Property Tax'), 'COURT_CASE', 1, now(), now(), false);

INSERT INTO eg_demand_reason_master
(id, reasonmaster, category, isdebit, module, code, "order", create_date, modified_date, isdemand)
VALUES(nextval('seq_eg_demand_reason_master'), 'Write Off', (select id from eg_reason_category where code='WRITEOFF'), 'N', (select id from eg_module where name = 'Property Tax'), 'WRITE_OFF', 1, now(), now(), false);

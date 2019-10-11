INSERT INTO eg_demand_reason_master
(id, reasonmaster, category, isdebit, module, code, "order", create_date, modified_date, isdemand)
VALUES(nextval('seq_eg_demand_reason_master'), 'Early Payment Rebate', (select id from eg_reason_category where code='REBATE'), 'N', (select id from eg_module where name = 'Property Tax'), 'REBATE', 1, now(), now(), false);


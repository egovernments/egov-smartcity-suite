------------------START------------------
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Contingencies', 'CAPITAL', null, 'Contingency expenses', 1, now(), 1, now());
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Petty Supervision Charges', 'CAPITAL', null, 'Petty Supervision Charges', 1, now(), 1, now());
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Preparation of Plan and Estimates', 'CAPITAL', null, 'Preparation of Plan and Estimates', 1, now(), 1, now());
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Quality Control', 'CAPITAL', null, 'Quality Control', 1, now(), 1, now());
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Superior Supervision Charges', 'CAPITAL', null, 'Superior Supervision Charges', 1, now(), 1, now());
INSERT INTO egw_overhead(id, name, expenditure_type, accountcode_id, description, created_by, created_date, modified_by, modified_date)
	VALUES (NEXTVAL('SEQ_EGW_OVERHEAD'), 'Highways Charges', 'CAPITAL', null, 'Highways Charges', 1, now(), 1, now());
-------------------END-------------------

------------------START------------------	
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Contingencies'), 0, 2.5, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Petty Supervision Charges'), 0, 2.5, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Preparation of Plan and Estimates'), 0, 2.5, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Quality Control'), 0, 1, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Superior Supervision Charges'), 0, 10, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
INSERT INTO egw_overhead_rate(id, overhead_id, lumpsum_amount, percentage, startdate, enddate, my_ohr_index)
    VALUES (NEXTVAL('SEQ_EGW_OVERHEAD_RATE'), (select id from egw_overhead where name = 'Highways Charges'), 10000, 0, to_date('01-04-2015','DD-MM-YYYY'), null, 0);
-------------------END-------------------

--rollback delete from EGW_OVERHEAD_RATE;
--rollback delete from EGW_OVERHEAD;


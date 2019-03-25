INSERT INTO eg_reason_category (id, "name", code, "order", modified_date)
VALUES(nextval('seq_eg_reason_category'), 'Write Off', 'WRITEOFF', (select "order" from eg_reason_category order by id desc limit 1)+1, now());

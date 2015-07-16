INSERT INTO eg_module VALUES (nextval('seq_eg_module'), 'Receipt Services', true, null, (select id from eg_module where name='Collection Transaction'), 'Receipt Services', 1);

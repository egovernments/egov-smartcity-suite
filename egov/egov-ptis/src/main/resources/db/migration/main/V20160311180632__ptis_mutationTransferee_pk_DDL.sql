ALTER TABLE egpt_mutation_transferee ADD COLUMN id BIGINT;
CREATE SEQUENCE seq_egpt_mutation_transferee;
UPDATE egpt_mutation_transferee SET id = nextval('seq_egpt_mutation_transferee');
ALTER TABLE egpt_mutation_transferee ALTER COLUMN id SET NOT NULL;
ALTER TABLE egpt_mutation_transferee ADD PRIMARY KEY (id);

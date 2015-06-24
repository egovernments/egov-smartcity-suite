delete from egpt_mutation_master where type='TRANSFER';
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Sale Deed', 'Sale Deed','TRANSFER','SALE',1);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Gift Deed', 'Gift Deed','TRANSFER','GIFT',2);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Will Deed', 'Will Deed','TRANSFER','WILL',3);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Court Deed', 'Court Deed','TRANSFER','COURT',4);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Settlement Deed', 'Settlement Deed','TRANSFER','SETTLEMENT',5);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Hakku vidudhala', 'Hakku Vidudhala','TRANSFER','HAKKUVIDLA',6);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Dasthavidhulu', 'Dasthavidhulu','TRANSFER','DASTHAVIDLA',7);
INSERT INTO egpt_mutation_master values (nextval('SEQ_EGPT_MUTATION_MASTER'), 'Legal Heir', 'Legal Heir','TRANSFER','LEGALHEIR',8);
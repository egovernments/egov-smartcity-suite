ALTER TABLE pgr_receiving_center ADD COLUMN isCrnRequired bool default false;
--rollback ALTER TABLE pgr_receiving_center DROP COLUMN isCrnRequired;
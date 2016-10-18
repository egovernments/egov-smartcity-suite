----------------- egmrs_registration ---------------
ALTER TABLE egmrs_registration ALTER COLUMN state_id DROP NOT NULL;

ALTER TABLE egmrs_registration ADD COLUMN islegacy boolean DEFAULT false;

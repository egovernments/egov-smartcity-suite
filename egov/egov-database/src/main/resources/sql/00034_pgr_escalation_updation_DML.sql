ALTER TABLE pgr_complaint ADD COLUMN escalation_date TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE pgr_escalation ADD COLUMN no_of_hrs INTEGER;

ALTER TABLE pgr_complainttype DROP COLUMN hrs_to_resolve;

--rollback ALTER TABLE pgr_complaint DROP COLUMN escalation_date;
--rollback ALTER TABLE pgr_escalation DROP COLUMN no_of_hrs;
--rollback ALTER TABLE pgr_complainttype DROP COLUMN hrs_to_resolve;

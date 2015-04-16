ALTER TABLE pgr_complainttype RENAME COLUMN days_to_resolve TO hrs_to_resolve;

--rollback ALTER TABLE pgr_complainttype RENAME COLUMN hrs_to_resolve TO days_to_resolve;

ALTER TABLE pgr_complainttype ADD COLUMN description char varying(100);
--rollback ALTER TABLE pgr_complainttype DROP COLUMN description;
ALTER TABLE pgr_complainttype ADD CONSTRAINT uk_complainttype_code UNIQUE (code);
--rollback ALTER TABLE pgr_complainttype DROP CONSTRAINT uk_complainttype_code;
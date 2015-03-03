ALTER table pgr_complaintstatus_mapping rename column complaintstatus_id TO current_status_id;
ALTER table pgr_complaintstatus_mapping add column show_status_id bigint;
ALTER table pgr_complaintstatus_mapping add CONSTRAINT fk_complainant_status foreign key (show_status_id) 
REFERENCES pgr_complaintstatus (id);

--rollback alter table pgr_complaintstatus_mapping drop constraint fk_complainant_status;
--rollback alter table pgr_complaintstatus_mapping drop column show_status_id;
--rollback alter table pgr_complaintstatus_mapping rename column current_status_id TO complaintstatus_id;

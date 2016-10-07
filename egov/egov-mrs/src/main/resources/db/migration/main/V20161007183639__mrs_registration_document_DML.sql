

ALTER TABLE egmrs_applicant DROP COLUMN photo;
ALTER TABLE egmrs_applicant DROP COLUMN signature;

alter table egmrs_applicant add column photoFileStore  bigint ;
alter table egmrs_applicant add column signatureFileStore  bigint;

ALTER TABLE egmrs_witness DROP COLUMN photo;
ALTER TABLE egmrs_witness DROP COLUMN signature;

alter table egmrs_witness add column photoFileStore  bigint ;
alter table egmrs_witness add column signatureFileStore  bigint ;
alter table EGPT_objection  DROP COLUMN OBJECTION_REJECTED CASCADE;
alter table EGPT_objection add column OBJECTION_REJECTED  BOOLEAN;

------rename table names
ALTER TABLE egpt_objection_docs
RENAME column objection TO petition;

ALTER TABLE egpt_objection_docs
RENAME TO egpt_petition_docs;

ALTER TABLE egpt_objection
ADD column disposal_date timestamp without time zone;

ALTER TABLE egpt_objection
RENAME TO egpt_petition;

ALTER SEQUENCE SEQ_EGPT_OBJECTION RENAME TO SEQ_EGPT_PETITION;

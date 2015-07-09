DROP SEQUENCE SEQ_DRAWING_OFFICER;
CREATE SEQUENCE SEQ_EG_DRAWINGOFFICER;
ALTER TABLE eg_drawingofficer ADD COLUMN position BIGINT;
ALTER TABLE eg_drawingofficer ADD CONSTRAINT FK_EG_DRAWINGOFFICER_POSITION FOREIGN KEY(position)
REFERENCES eg_position(id);
ALTER TABLE eg_drawingofficer  RENAME COLUMN id_bank TO bank;
ALTER TABLE eg_drawingofficer  RENAME COLUMN id_branch TO bankbranch;
ALTER TABLE eg_drawingofficer  RENAME COLUMN account_number TO accountnumber;

ALTER TABLE eg_drawingofficer ADD COLUMN version BIGINT;
ALTER TABLE eg_drawingofficer ADD COLUMN createddate timestamp without time zone;
ALTER TABLE eg_drawingofficer ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eg_drawingofficer ADD COLUMN createdby BIGINT;
ALTER TABLE eg_drawingofficer ADD COLUMN lastmodifiedby BIGINT;
UPDATE eg_drawingofficer SET lastmodifiedby=1, createdby=1, createddate='01-01-2015',lastmodifieddate='01-01-2015',version=0;

--rollback DROP SEQUENCE SEQ_EG_DRAWINGOFFICER;
--rollback CREATE SEQUENCE SEQ_DRAWING_OFFICER;
--rollback ALTER TABLE eg_drawingofficer DROP COLUMN position;
--rollback ALTER TABLE eg_drawingofficer  RENAME COLUMN designation TO desig_id;
--rollback ALTER TABLE eg_drawingofficer  RENAME COLUMN department TO dept_id;
--rollback ALTER TABLE eg_drawingofficer  RENAME COLUMN sanctionedposts TO sanctioned_posts;
--rollback ALTER TABLE eg_drawingofficer  RENAME COLUMN outsourcedposts TO outsourced_posts;

--rollback ALTER TABLE eg_drawingofficer DROP COLUMN version;
--rollback ALTER TABLE eg_drawingofficer DROP COLUMN createddate;
--rollback ALTER TABLE eg_drawingofficer DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE eg_drawingofficer DROP COLUMN createdby;
--rollback ALTER TABLE eg_drawingofficer DROP COLUMN lastmodifiedby;

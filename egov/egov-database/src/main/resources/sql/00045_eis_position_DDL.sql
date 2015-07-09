ALTER SEQUENCE SEQ_POS RENAME TO SEQ_EG_POSITION;

ALTER TABLE eg_position RENAME COLUMN POSITION_NAME TO name;
ALTER TABLE eg_position RENAME COLUMN id_deptdesig TO deptDesig;

ALTER TABLE eg_position DROP COLUMN createddate;
ALTER TABLE eg_position DROP COLUMN id_drawing_officer;
ALTER TABLE eg_position DROP COLUMN createdby;
ALTER TABLE eg_position DROP COLUMN modifiedby;
ALTER TABLE eg_position DROP COLUMN modifieddate;
ALTER TABLE eg_position DROP COLUMN ISPOST_OUTSOURCED;

ALTER TABLE eg_position ADD COLUMN createddate timestamp without time zone;
ALTER TABLE eg_position ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eg_position ADD COLUMN createdby BIGINT;
ALTER TABLE eg_position ADD COLUMN lastmodifiedby BIGINT;
ALTER TABLE eg_position ADD COLUMN isPostOutsourced BOOLEAN;
ALTER TABLE eg_position ADD COLUMN version BIGINT;

update eg_position set lastmodifiedby=1, createdby=1,isPostOutsourced=false, createddate='01-01-2015',lastmodifieddate='01-01-2015'
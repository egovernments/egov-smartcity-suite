ALTER TABLE egeis_deptdesig  RENAME COLUMN desig_id TO designation;
ALTER TABLE egeis_deptdesig  RENAME COLUMN dept_id TO department;
ALTER TABLE egeis_deptdesig  RENAME COLUMN sanctioned_posts TO sanctionedposts;
ALTER TABLE egeis_deptdesig  RENAME COLUMN outsourced_posts TO outsourcedposts;

ALTER TABLE egeis_deptDesig ADD COLUMN version BIGINT;
ALTER TABLE egeis_deptDesig ADD COLUMN createddate timestamp without time zone;
ALTER TABLE egeis_deptDesig ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE egeis_deptDesig ADD COLUMN createdby BIGINT;
ALTER TABLE egeis_deptDesig ADD COLUMN lastmodifiedby BIGINT;
UPDATE egeis_deptDesig SET lastmodifiedby=1, createdby=1, createddate='01-01-2015',lastmodifieddate='01-01-2015',version=0;

--rollback ALTER TABLE egeis_deptdesig  RENAME COLUMN designation TO desig_id;
--rollback ALTER TABLE egeis_deptdesig  RENAME COLUMN department TO dept_id;
--rollback ALTER TABLE egeis_deptdesig  RENAME COLUMN sanctionedposts TO sanctioned_posts;
--rollback ALTER TABLE egeis_deptdesig  RENAME COLUMN outsourcedposts TO outsourced_posts;

--rollback ALTER TABLE DROP COLUMN version;
--rollback ALTER TABLE DROP COLUMN createddate;
--rollback ALTER TABLE DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE DROP COLUMN createdby;
--rollback ALTER TABLE DROP COLUMN lastmodifiedby;

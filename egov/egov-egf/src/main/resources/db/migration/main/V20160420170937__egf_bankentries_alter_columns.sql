
ALTER TABLE bankentries ALTER COLUMN type TYPE varchar(20);

ALTER TABLE bankentries ADD COLUMN version bigint;

ALTER TABLE bankentries_mis RENAME COLUMN bankentries_id TO bankentriesid;

ALTER TABLE bankentries_mis RENAME COLUMN function_id TO functionid;

ALTER TABLE bankentries_mis ADD COLUMN fundId bigint;

ALTER TABLE bankentries_mis ADD COLUMN departmentid bigint;

ALTER TABLE bankentries_mis ADD COLUMN fundsourceid bigint;

ALTER TABLE bankentries_mis ADD COLUMN divisionid bigint;

ALTER TABLE bankentries_mis ADD COLUMN schemeid bigint;

ALTER TABLE bankentries_mis ADD COLUMN subschemeid bigint;

ALTER TABLE bankentries_mis ADD COLUMN functionaryid bigint;

ALTER TABLE bankentries_mis ADD COLUMN version bigint;

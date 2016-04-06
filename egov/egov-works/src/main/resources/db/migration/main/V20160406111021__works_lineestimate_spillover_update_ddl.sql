-------------- New column spill over flag to recognize it line estimate is spill over ------------------
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN spilloverflag boolean default 'false';

UPDATE EGW_LINEESTIMATE SET billscreated = 'false';
UPDATE EGW_LINEESTIMATE SET workordercreated = 'false';

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN spilloverflag;
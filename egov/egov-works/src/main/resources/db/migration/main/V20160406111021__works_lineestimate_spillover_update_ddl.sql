-------------- New column spill over flag to recognize it line estimate is spill over ------------------
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN spilloverflag boolean DEFAULT 'false';

ALTER TABLE EGW_LINEESTIMATE ALTER COLUMN billscreated SET DEFAULT 'false';
ALTER TABLE EGW_LINEESTIMATE ALTER COLUMN workordercreated SET DEFAULT 'false';

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN spilloverflag;
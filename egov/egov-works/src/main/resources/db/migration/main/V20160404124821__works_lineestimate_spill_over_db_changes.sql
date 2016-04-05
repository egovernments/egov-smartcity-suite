------------------START--------------------------
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN grossamountbilled double precision;

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN workordercreated boolean;
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN billscreated boolean;

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN isbillscreated;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN isworkordercreated;

--rollback ALTER TABLE EGW_LINEESTIMATE_DETAILS DROP COLUMN grossamountbilled;

-----------------END------------------------------
---------START---------------
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN councilresolutionnumber character varying(100) ;
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN councilresolutiondate timestamp without time zone;

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN councilresolutionnumber;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN councilresolutiondate;

---------END-----------------
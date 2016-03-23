---------START---------------
ALTER TABLE egw_lineestimate ALTER COLUMN adminsanctionnumber TYPE character varying(50);

--rollback ALTER TABLE egw_lineestimate ALTER COLUMN adminsanctionnumber TYPE VARCHAR(50);

---------END-----------------
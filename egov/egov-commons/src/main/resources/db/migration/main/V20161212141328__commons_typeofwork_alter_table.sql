------------------START------------------
ALTER TABLE egw_typeofwork ALTER COLUMN code TYPE character varying(50);
ALTER TABLE egw_typeofwork RENAME COLUMN "description" to "name";
ALTER TABLE egw_typeofwork ALTER COLUMN "name" TYPE character varying(100);
ALTER TABLE egw_typeofwork ADD COLUMN "description" character varying(1024);
ALTER TABLE egw_typeofwork ADD COLUMN "active" boolean DEFAULT true;
ALTER TABLE egw_typeofwork ADD COLUMN "version" bigint DEFAULT 0;

--rollback ALTER TABLE egw_typeofwork DROP COLUMN "version";
--rollback ALTER TABLE egw_typeofwork DROP COLUMN "active";
--rollback ALTER TABLE egw_typeofwork DROP COLUMN "description";
--rollback ALTER TABLE egw_typeofwork RENAME COLUMN "name" to "description";
--rollback ALTER TABLE egw_typeofwork ALTER COLUMN "description" character varying(1000);
--rollback ALTER TABLE egw_typeofwork ALTER COLUMN code TYPE character varying(20);
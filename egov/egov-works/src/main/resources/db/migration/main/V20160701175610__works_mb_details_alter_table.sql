------------------START------------------
ALTER TABLE egw_mb_details ALTER COLUMN quantity TYPE double precision;
ALTER TABLE egw_mb_details ALTER COLUMN rate TYPE double precision;
ALTER TABLE egw_mb_details ALTER COLUMN amount TYPE double precision;

--rollback ALTER TABLE egw_mb_details ALTER COLUMN quantity TYPE bigint;
--rollback ALTER TABLE egw_mb_details ALTER COLUMN rate TYPE bigint;
--rollback ALTER TABLE egw_mb_details ALTER COLUMN amount TYPE bigint;
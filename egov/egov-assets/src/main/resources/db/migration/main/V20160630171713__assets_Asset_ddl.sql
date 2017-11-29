---------------------------------------------------------- START ----------------------------------------------------------------------------
ALTER TABLE egasset_asset ADD COLUMN accDepreciation bigint;

ALTER TABLE egasset_asset ADD COLUMN properties jsonb;

ALTER TABLE egasset_asset ADD COLUMN grossValue bigint;
------------------------------------------------------------ END --------------------------------------------------------------------------

--rollback ALTER TABLE egasset_asset DROP COLUMN accDepreciation;
--rollback ALTER TABLE egasset_asset DROP COLUMN properties;
--rollback ALTER TABLE egasset_asset DROP COLUMN grossValue;
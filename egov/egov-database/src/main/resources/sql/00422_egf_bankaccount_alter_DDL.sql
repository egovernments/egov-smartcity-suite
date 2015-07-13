ALTER TABLE bankaccount DROP COLUMN created RESTRICT;
ALTER TABLE bankaccount DROP COLUMN lastmodified RESTRICT;
ALTER TABLE bankaccount DROP COLUMN modifiedby RESTRICT;
ALTER TABLE bankaccount ADD createdby bigint;
ALTER TABLE bankaccount ADD lastmodifiedby bigint;
ALTER TABLE bankaccount ADD createddate timestamp without time zone;
ALTER TABLE bankaccount ADD lastmodifieddate timestamp without time zone;
ALTER TABLE bankaccount ADD COLUMN version bigint;

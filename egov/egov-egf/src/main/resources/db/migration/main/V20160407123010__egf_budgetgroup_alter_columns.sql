ALTER TABLE egf_budgetgroup RENAME COLUMN updatedtimestamp to createddate;

ALTER TABLE egf_budgetgroup ADD COLUMN createdby bigint;

ALTER TABLE egf_budgetgroup ADD COLUMN lastmodifiedby bigint;

ALTER TABLE egf_budgetgroup ADD COLUMN lastmodifieddate timestamp without time zone;

ALTER TABLE egf_budgetgroup  ADD COLUMN version numeric ;

update egf_budgetgroup set version = 0 ;

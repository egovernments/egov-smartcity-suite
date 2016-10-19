ALTER TABLE egf_budget ADD COLUMN lastmodifieddate timestamp without time zone;

ALTER TABLE egf_budget  ADD COLUMN version numeric ;

update egf_budget set version = 0 ;


ALTER TABLE EG_UOMCATEGORY RENAME COLUMN lastmodified TO lastmodifieddate;

ALTER TABLE EG_UOMCATEGORY  ADD COLUMN version numeric ;

update EG_UOMCATEGORY set version=0 where version is null;
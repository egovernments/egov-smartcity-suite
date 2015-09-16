alter table  EGADTAX_RATES  add column createddate timestamp without time zone NOT NULL default CURRENT_DATE;
alter table  EGADTAX_RATES  add column lastmodifieddate timestamp without time zone;
alter table  EGADTAX_RATES  add column createdby bigint NOT NULL;
alter table  EGADTAX_RATES  add column lastmodifiedby bigint;

alter table  EGADTAX_HOARDING  add column createddate timestamp without time zone NOT NULL default CURRENT_DATE;
alter table  EGADTAX_HOARDING  add column lastmodifieddate timestamp without time zone;
alter table  EGADTAX_HOARDING  add column createdby bigint NOT NULL;
alter table  EGADTAX_HOARDING  add column lastmodifiedby bigint;

alter table  EGADTAX_CATEGORY  add column lastmodifieddate timestamp without time zone;
alter table  EGADTAX_CATEGORY  add column lastmodifiedby bigint;

alter table  egadtax_SUBCATEGORY  add column lastmodifieddate timestamp without time zone;
alter table  egadtax_SUBCATEGORY  add column lastmodifiedby bigint;

alter table  egadtax_UnitOfMeasure  add column lastmodifieddate timestamp without time zone;
alter table  egadtax_UnitOfMeasure  add column lastmodifiedby bigint;
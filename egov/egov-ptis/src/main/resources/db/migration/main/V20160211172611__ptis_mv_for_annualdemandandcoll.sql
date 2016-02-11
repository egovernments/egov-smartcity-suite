drop table if exists egpt_mv_annual_demand_coll;
drop table if exists egpt_mv_esuvidha_annual_coll;

CREATE TABLE egpt_mv_annual_demand_coll(
id_basic_property bigint NOT NULL,
annualTax double precision,
annualcoll double precision
);

CREATE TABLE egpt_mv_esuvidha_annual_coll(
id_basic_property bigint NOT NULL,
coll double precision
);

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists annualdemand;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN annualdemand double precision;

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists annualcoll;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN annualcoll double precision;
CREATE TABLE egpt_mv_annual_demand_coll(
id_basic_property bigint NOT NULL,
annualTax double precision,
annualcoll double precision
);

CREATE TABLE egpt_mv_esuvidha_annual_coll(
id_basic_property bigint NOT NULL,
coll double precision
);

ALTER TABLE egpt_mv_propertyinfo ADD COLUMN annualdemand double precision;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN annualcoll double precision;
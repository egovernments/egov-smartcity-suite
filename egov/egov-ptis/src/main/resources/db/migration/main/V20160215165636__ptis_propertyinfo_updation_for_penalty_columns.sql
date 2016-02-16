drop table if exists egpt_mv_current_penalty_dem_coll;
drop table if exists egpt_mv_arrear_penalty_dem_coll;
drop table if exists egpt_mv_penalty_arrcurr_dem_coll;

CREATE TABLE egpt_mv_current_penalty_dem_coll(
id_basic_property bigint,
penalty_demand double precision,
penalty_collected double precision);

CREATE TABLE egpt_mv_arrear_penalty_dem_coll(
id_basic_property bigint,
penalty_demand double precision,
penalty_collected double precision);

CREATE TABLE egpt_mv_penalty_arrcurr_dem_coll(
basicpropertyid bigint,
pen_aggr_current_demand double precision,
pen_aggr_current_coll double precision,
pen_aggr_arrear_demand double precision,
pen_aggr_arr_coll double precision);

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists pen_aggr_current_demand;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN pen_aggr_current_demand double precision;

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists pen_aggr_current_coll;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN pen_aggr_current_coll double precision;

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists pen_aggr_arrear_demand;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN pen_aggr_arrear_demand double precision;

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists pen_aggr_arr_coll;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN pen_aggr_arr_coll double precision;
ALTER TABLE egpt_mv_inst_dem_coll ADD COLUMN balance double precision;


--columns default to 0
alter table egpt_mv_inst_dem_coll alter column generaltax set default 0;
alter table egpt_mv_inst_dem_coll alter column libcesstax set default 0;
alter table egpt_mv_inst_dem_coll alter column educesstax set default 0;
alter table egpt_mv_inst_dem_coll alter column unauthpenaltytax set default 0;
alter table egpt_mv_inst_dem_coll alter column penaltyfinestax set default 0;
alter table egpt_mv_inst_dem_coll alter column sewtax set default 0;
alter table egpt_mv_inst_dem_coll alter column vacantlandtax set default 0;
alter table egpt_mv_inst_dem_coll alter column pubserchrgtax set default 0;
alter table egpt_mv_inst_dem_coll alter column generaltaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column libcesstaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column educesstaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column unauthpenaltytaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column penaltyfinestaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column sewtaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column vacantlandtaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column pubserchrgtaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column gen_tax set default 0;
alter table egpt_mv_inst_dem_coll alter column gen_tax_coll set default 0;
alter table egpt_mv_inst_dem_coll alter column scavengetax set default 0;
alter table egpt_mv_inst_dem_coll alter column scavengetaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column drainagetax set default 0;
alter table egpt_mv_inst_dem_coll alter column drainagetaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column lighttax set default 0;
alter table egpt_mv_inst_dem_coll alter column lighttaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column watertax set default 0;
alter table egpt_mv_inst_dem_coll alter column watertaxcoll set default 0;
alter table egpt_mv_inst_dem_coll alter column balance set default 0;


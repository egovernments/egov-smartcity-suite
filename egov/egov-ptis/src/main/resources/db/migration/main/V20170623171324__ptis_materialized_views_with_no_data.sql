CREATE MATERIALIZED VIEW egpt_view_propertyinfo 
AS
  select * from egpt_mv_propertyinfo
WITH NO DATA;

create index idx_egptpmv_upicno on egpt_view_propertyinfo(upicno);
create index idx_egptpmv_basicpropertyid on egpt_view_propertyinfo(basicpropertyid);

CREATE MATERIALIZED VIEW egpt_view_inst_dem_coll 
AS
  select * from egpt_mv_inst_dem_coll
WITH NO DATA;

create index idx_egptidc_basicpropertyid on egpt_view_inst_dem_coll(id_basic_property);

CREATE MATERIALIZED VIEW egpt_view_current_floor_detail 
AS
  select * from egpt_mv_current_floor_detail
WITH NO DATA;

create index idx_egptcfd_basicpropertyid on egpt_view_current_floor_detail(basicpropertyid);

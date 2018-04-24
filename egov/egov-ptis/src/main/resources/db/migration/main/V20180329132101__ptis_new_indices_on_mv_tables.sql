--indices on various table columns for performance


drop index if exists idx_egptmvins_installment;
drop index if exists idx_egptflrdtl_propdtl;
drop index if exists idx_egptflrdtl_struct;
drop index if exists idx_egptflrdtl_usg;

create index idx_egptmvins_installment on egpt_mv_inst_dem_coll(id_installment);
create index idx_egptflrdtl_propdtl on egpt_floor_detail(id_property_detail);
create index idx_egptflrdtl_struct on egpt_floor_detail(id_struc_cl);
create index idx_egptflrdtl_usg on egpt_floor_detail(id_usg_mstr);


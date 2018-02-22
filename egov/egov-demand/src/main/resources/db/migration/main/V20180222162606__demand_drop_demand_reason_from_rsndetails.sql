--drop column referring demand reason

delete from eg_demand_reason_details where id_demand_reason in (select id from eg_demand_reason where id_demand_reason_master in (select id from eg_demand_reason_master where module in (select id from eg_module where name<>'Property Tax')));

drop index if exists idx_dem_reason_det;

alter table eg_demand_reason_details drop constraint if exists fk_egpt_dem_reason_id;

alter table eg_demand_reason_details drop column id_demand_reason;

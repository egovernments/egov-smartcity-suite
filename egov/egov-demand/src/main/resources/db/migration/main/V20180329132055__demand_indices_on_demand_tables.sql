--indices on various table columns for performance

drop index if exists idx_egdm_installment;
drop index if exists idx_drm_module;
drop index if exists idx_drm_code;

create index idx_egdm_installment on eg_demand(id_installment);
create index idx_drm_module on eg_demand_reason_master(module);
create index idx_drm_code on eg_demand_reason_master(code);

alter table eg_demand_details alter column amount set default 0;

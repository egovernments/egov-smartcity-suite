--new column to map demand reason master

alter table eg_demand_reason_details add column id_demand_reason_master bigint;

alter table eg_demand_reason_details add constraint fk_eg_dem_reason_master_id foreign key (id_demand_reason_master) references eg_demand_reason_master (id);

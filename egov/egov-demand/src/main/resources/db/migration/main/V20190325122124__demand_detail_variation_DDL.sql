create table eg_demand_detail_variation (
id						bigint primary key not null,
demand_detail			bigint not null,
demand_reason_master	bigint not null,
dramount				double precision default 0,
cramount				double precision default 0,
remarks					character varying(512),
version         		numeric default 1,
createdby       		numeric default 1,
createddate     		timestamp without time zone default now(),
lastmodifiedby  		numeric default 1,
lastmodifieddate		timestamp without time zone default now()
);

create sequence seq_eg_demand_detail_variation;

alter table eg_demand_detail_variation add constraint fk_demand_detail foreign key (demand_detail) references eg_demand_details (id);

alter table eg_demand_detail_variation add constraint fk_demand_reason_master foreign key (demand_reason_master) references eg_demand_reason_master (id);

comment on column eg_demand_detail_variation.id is 'Primary key of table';
comment on column eg_demand_detail_variation.demand_detail is 'Id of eg_demand_detail';
comment on column eg_demand_detail_variation.demand_reason_master is 'Id of eg_demand_reason_master, reason for amount variation';
comment on column eg_demand_detail_variation.dramount is 'Decrease in demand or collection';
comment on column eg_demand_detail_variation.cramount is 'Increase in demand or collection';
comment on column eg_demand_detail_variation.remarks is 'To record remarks if any';
comment on column eg_demand_detail_variation.version is 'Version of the record';
comment on column eg_demand_detail_variation.createdby is 'Id of eg_user, who created';
comment on column eg_demand_detail_variation.createddate is 'Date of creation';
comment on column eg_demand_detail_variation.lastmodifiedby is 'Id of eg_user, who last modified';
comment on column eg_demand_detail_variation.lastmodifieddate is 'Date of last modification';

--rollback queries
--drop sequence seq_eg_demand_detail_variation;
--drop table eg_demand_detail_variation;

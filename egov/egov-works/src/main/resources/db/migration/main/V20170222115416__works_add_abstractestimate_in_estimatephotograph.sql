alter table EGW_ESTIMATE_PHOTOGRAPHS add column abstractestimate bigint;
alter table EGW_ESTIMATE_PHOTOGRAPHS add constraint fk_abstractestimate_id foreign key (abstractestimate) references egw_abstractestimate (id);

--rollback alter table EGW_ESTIMATE_PHOTOGRAPHS drop constraint fk_abstractestimate_id;
--rollback alter table EGW_ESTIMATE_PHOTOGRAPHS drop column "abstractestimate";
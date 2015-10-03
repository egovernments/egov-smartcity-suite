alter table eg_crosshierarchy add column parenttype bigint;
alter table eg_crosshierarchy add column childtype bigint;

alter table eg_crosshierarchy add constraint fk_crossheirarchy_parenttype foreign key (parenttype) references eg_boundary_type (id);
alter table eg_crosshierarchy add constraint fk_crossheirarchy_childtype foreign key (childtype) references eg_boundary_type (id);
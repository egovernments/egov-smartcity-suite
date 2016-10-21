delete from eg_crosshierarchy where (parent not in  (select id from eg_boundary) or child not in  (select id from eg_boundary));
alter table eg_boundary ADD CONSTRAINT parent_bndry_fk foreign key (parent) references eg_boundary(id);
alter table eg_crosshierarchy add constraint fk_crossheirarchy_parent foreign key (parent) references eg_boundary (id);
alter table eg_crosshierarchy add constraint fk_crossheirarchy_child foreign key (child) references eg_boundary (id);

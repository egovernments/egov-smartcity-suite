update eg_boundary set parent = 1 where boundarytype in (select id from eg_boundary_type where name = 'Ward' );

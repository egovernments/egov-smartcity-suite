
update eg_crosshierarchy ch set parenttype = (select boundarytype from eg_boundary where id = (select parent from eg_crosshierarchy where id = ch.id));
update eg_crosshierarchy ch set childtype = (select boundarytype from eg_boundary where id = (select child from eg_crosshierarchy where id = ch.id));
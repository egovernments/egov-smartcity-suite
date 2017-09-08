alter table eg_boundary rename isHistory to active;
update eg_boundary set active= NOT active;
update eg_boundary set version=0 where version is null;
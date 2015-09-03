alter table revinfo add column userid bigint;
alter table revinfo add column ipaddress varchar(20);
alter table revinfo rename revtstmp to "timestamp";
alter table revinfo rename rev to id;
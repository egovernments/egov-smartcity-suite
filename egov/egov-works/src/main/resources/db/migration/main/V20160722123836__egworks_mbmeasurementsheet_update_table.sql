------------Removing unused columns-------------------
alter table EGW_MB_MEASUREMENTSHEET drop column slNo;
alter table EGW_MB_MEASUREMENTSHEET drop column identifier;
alter table EGW_MB_MEASUREMENTSHEET drop column remarks;

--rollback alter table EGW_MB_MEASUREMENTSHEET add column remarks varchar(1024);
--rollbback alter table EGW_MB_MEASUREMENTSHEET add column identifier character(1);
--rollback alter table EGW_MB_MEASUREMENTSHEET add column slNo smallint;
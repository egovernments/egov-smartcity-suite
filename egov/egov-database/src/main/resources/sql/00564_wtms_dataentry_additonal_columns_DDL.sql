alter table egwtr_connectiondetails add legacy boolean;
alter table egwtr_existing_connection_details add readingDate date;

--rollback alter table egwtr_connectiondetails drop column legacy ;
--rollback alter table egwtr_connectiondetails drop column readingDate ;

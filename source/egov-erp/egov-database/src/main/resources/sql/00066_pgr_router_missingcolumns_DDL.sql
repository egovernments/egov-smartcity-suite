alter TABLE pgr_router  add column  createdby bigint;
alter TABLE pgr_router  add column  createddate date;
alter TABLE pgr_router  add column lastmodifiedby bigint;
alter TABLE pgr_router  add column lastmodifieddate date;

--rollback alter TABLE pgr_router  drop column  createdby;
--rollback alter TABLE pgr_router  drop column  createddate;
--rollback alter TABLE pgr_router  drop column  lastmodifiedby;
--rollback alter TABLE pgr_router  drop column  lastmodifieddate;


alter table eg_location rename isactive to active;
alter table eg_location drop column locationid, drop column islocation, drop column createddate, drop column lastmodifieddate;
alter table eg_location add column "version" numeric default 0;
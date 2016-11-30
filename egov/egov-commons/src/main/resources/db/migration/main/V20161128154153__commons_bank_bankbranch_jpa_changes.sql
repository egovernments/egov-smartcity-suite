alter table bank add column version bigint default 0;
alter table bank add column createdby bigint;
alter table bank rename column created TO createddate;
alter table bank rename column modifiedby TO lastmodifiedby;
alter table bank rename column lastmodified TO lastmodifieddate;


alter table bankbranch add column version bigint default 0;
alter table bankbranch add column createdby bigint;
alter table bankbranch rename column created TO createddate;
alter table bankbranch rename column modifiedby TO lastmodifiedby;
alter table bankbranch rename column lastmodified TO lastmodifieddate;

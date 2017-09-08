alter table egcncl_councilsequence add column createddate date default now();
alter table egcncl_councilsequence add column lastmodifieddate date default now();
alter table egcncl_councilsequence add column lastmodifiedby bigint default 1;
alter table egcncl_councilsequence add column version bigint DEFAULT 0;
alter table egcncl_councilsequence add column  createdby bigint default 1;
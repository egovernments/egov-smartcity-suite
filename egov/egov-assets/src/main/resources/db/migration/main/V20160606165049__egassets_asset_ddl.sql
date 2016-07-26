alter table EGASSET_ASSET add column version numeric;

alter table EGASSET_ASSET add column createdby  bigint;

alter table EGASSET_ASSET add column lastmodifiedby  bigint;

alter table EGASSET_ASSET add column createddate  date;

alter table EGASSET_ASSET add column lastmodifieddate date;


alter table EGASSET_ASSET drop column created_by;
alter table EGASSET_ASSET drop column modified_by;
alter table EGASSET_ASSET drop column created_date;
alter table EGASSET_ASSET drop column modified_date;
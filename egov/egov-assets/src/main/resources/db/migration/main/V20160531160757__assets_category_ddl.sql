alter table EGASSET_ASSET_CATEGORY add column version numeric;

alter table EGASSET_ASSET_CATEGORY add column createdby  bigint;

alter table EGASSET_ASSET_CATEGORY add column lastmodifiedby  bigint;

alter table EGASSET_ASSET_CATEGORY add column createddate  date;

alter table EGASSET_ASSET_CATEGORY add column lastmodifieddate date;


alter table EGASSET_ASSET_CATEGORY drop column created_by;
alter table EGASSET_ASSET_CATEGORY drop column modified_by;
alter table EGASSET_ASSET_CATEGORY drop column created_date;
alter table EGASSET_ASSET_CATEGORY drop column modified_date;
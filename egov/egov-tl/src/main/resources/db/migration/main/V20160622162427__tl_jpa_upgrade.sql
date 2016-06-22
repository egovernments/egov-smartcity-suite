alter table egtl_document add column license bigint;
alter table egtl_license drop column license_type;
alter table egtl_license drop column id_objection;
alter table egtl_license drop column id_licensetransfer;
drop table egtl_tradelicense_docs cascade;
alter table egtl_mstr_status  drop COLUMN lastupdatedtimestamp;
alter table egtl_licensee add column version numeric default 0;
alter table egtl_mstr_status add column version numeric  default 0;
alter table egtl_mstr_license_type add column version numeric default 0;
alter table egtl_mstr_license_sub_type add column version numeric default 0;
alter table egtl_subcategory_details add column version numeric default 0;
alter table egtl_mstr_business_nature  add column version numeric default 0;
alter table egtl_license add column version numeric default 0;



--update category/subcategory code
update egtl_mstr_category  SET code = LPAD(id::text,5,'0');

update egtl_mstr_sub_category  SET code = LPAD(id::text,5,'0');

alter table egtl_mstr_category  ALTER  code TYPE character varying (5);

alter table egtl_mstr_sub_category ALTER  code TYPE character varying (5);

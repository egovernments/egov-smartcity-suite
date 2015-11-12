DELETE FROM egpt_mstr_bndry_category;
DELETE FROM egpt_mstr_category;

INSERT INTO egpt_mstr_category (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('Rcc Posh','.',''),' ',''),',',''),1,4)) || round(random()*1000),1 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='Rcc Posh') from egpt_property_usage_master pu;

INSERT INTO egpt_mstr_category (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('Rcc Ordinary','.',''),' ',''),',',''),1,4)) || round(random()*1000),2 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='Rcc Ordinary') from egpt_property_usage_master pu;

INSERT INTO egpt_mstr_category (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('M-terras','.',''),' ',''),',',''),1,4)) || round(random()*1000),3 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='M-terras') from egpt_property_usage_master pu;
  
INSERT INTO egpt_mstr_category (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('M-tiled/ac Sheet/gi Roof','.',''),' ',''),',',''),1,4)) || round(random()*1000),4 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='M-tiled/ac Sheet/gi Roof') from egpt_property_usage_master pu;

INSERT INTO egpt_mstr_category  (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('Country Tiled','.',''),' ',''),',',''),1,4)) || round(random()*1000),5 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='Country Tiled') from egpt_property_usage_master pu;

INSERT INTO egpt_mstr_category  (id, category_name, category_amnt, id_installment, is_history, created_by, id_usage, from_date, to_date, modified_by, created_date, modified_date, id_struct_cl) select nextval('seq_egpt_mstr_category'), upper(substr(replace(replace(replace(pu.usg_name,'.',''),' ',''),',',''),1,4)) || '' || 
upper(substr(replace(replace(replace('Huts','.',''),' ',''),',',''),1,4)) || round(random()*1000),6 ,NULL, 'N', 1, pu.id, to_date('01-04-04','DD-MM-YY'), to_date('31-03-2099','DD-MM-YY'), 1, now(), now(), (select id from egpt_struc_cl where constr_type='Huts') from egpt_property_usage_master pu;

Insert into EGPT_MSTR_BNDRY_CATEGORY select nextval('seq_egpt_mstr_bndry_category'),(select id from eg_boundary where name = 'Zone-1' and boundarytype = 
(select id from eg_boundary_type where name = 'Zone')), bndryCat.id, now() ,to_date('01-04-04','DD-MM-YY'),to_date('31-03-2099','DD-MM-YYYY'),1,1,'2015-07-15' from egpt_mstr_category bndryCat;

Insert into EGPT_MSTR_BNDRY_CATEGORY select nextval('seq_egpt_mstr_bndry_category'),(select id from eg_boundary where name = 'Zone-2' and boundarytype = 
(select id from eg_boundary_type where name = 'Zone')), bndryCat.id, now() ,to_date('01-04-04','DD-MM-YY'),to_date('31-03-2099','DD-MM-YYYY'),1,1,'2015-07-15' from egpt_mstr_category bndryCat;

Insert into EGPT_MSTR_BNDRY_CATEGORY select nextval('seq_egpt_mstr_bndry_category'),(select id from eg_boundary where name = 'Zone-3' and boundarytype = 
(select id from eg_boundary_type where name = 'Zone')), bndryCat.id, now() ,to_date('01-04-04','DD-MM-YY'),to_date('31-03-2099','DD-MM-YYYY'),1,1,'2015-07-15' from egpt_mstr_category bndryCat;

Insert into EGPT_MSTR_BNDRY_CATEGORY select nextval('seq_egpt_mstr_bndry_category'),(select id from eg_boundary where name = 'Zone-4' and boundarytype = 
(select id from eg_boundary_type where name = 'Zone')), bndryCat.id, now() ,to_date('01-04-04','DD-MM-YY'),to_date('31-03-2099','DD-MM-YYYY'),1,1,'2015-07-15' from egpt_mstr_category bndryCat;
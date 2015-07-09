--MASTER DATA FOR egpt_floor_type
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Bhetancherlla','1');
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Cement','2');
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Ceramic Tiles','3');
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Cudapha Slab','4');
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Marble','5');
INSERT INTO egpt_floor_type (id,name,code) values(nextval('seq_egpt_floor_type'),'Mosaic','6');

--MASTER DATA FOR egpt_roof_type
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'A.C.Sheet','1');
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'Country Tiled','2');
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'G.I.Sheet','3');
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'Madras Terraced','4');
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'Mangalore Tiled','5');
INSERT INTO egpt_roof_type (id,name,code) values(nextval('seq_egpt_roof_type'),'Ordinary RCC','6');

--MASTER DATA FOR egpt_wall_type
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'Boom Walls','1');
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'Bricks With Cement','2');
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'Bricks With Mud','3');
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'C.C.Bricks','4');
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'Mud','5');
INSERT INTO egpt_wall_type (id,name,code) values(nextval('seq_egpt_wall_type'),'Bcw','6');

--MASTER DATA FOR egpt_wood_type
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'Country Wood','1');
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'RoseWood','2');
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'Teak Wood','3');
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'Od','4');
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'Odb','6');
INSERT INTO egpt_wood_type (id,name,code) values(nextval('seq_egpt_wood_type'),'Of','7');

DELETE FROM egpt_occupation_type_master;
--egpt_occupation_type_master
Insert into egpt_occupation_type_master (ID,OCCUPATION,MODIFIED_DATE,OCCUPANY_FACTOR,OCCUPATION_LOCAL,CODE,FROM_DATE,TO_DATE,ID_USG_MSTR,CREATED_BY,
MODIFIED_BY,CREATED_DATE) values (nextval('SEQ_EGPT_OCCUPATION_TYPE_MASTER'),'Commercial',CURRENT_TIMESTAMP,1,null,'COMMERCIAL',to_date('01-04-10','DD-MM-RR'),
to_date('31-03-20','DD-MM-RR'),null,1,1,CURRENT_TIMESTAMP);
Insert into egpt_occupation_type_master (ID,OCCUPATION,MODIFIED_DATE,OCCUPANY_FACTOR,OCCUPATION_LOCAL,CODE,FROM_DATE,TO_DATE,ID_USG_MSTR,CREATED_BY,
MODIFIED_BY,CREATED_DATE) values (nextval('SEQ_EGPT_OCCUPATION_TYPE_MASTER'),'Owner',CURRENT_TIMESTAMP,2,null,'OWNER',to_date('01-04-10','DD-MM-RR'),
to_date('31-03-20','DD-MM-RR'),null,1,1,CURRENT_TIMESTAMP);
Insert into egpt_occupation_type_master (ID,OCCUPATION,MODIFIED_DATE,OCCUPANY_FACTOR,OCCUPATION_LOCAL,CODE,FROM_DATE,TO_DATE,ID_USG_MSTR,CREATED_BY,
MODIFIED_BY,CREATED_DATE) values (nextval('SEQ_EGPT_OCCUPATION_TYPE_MASTER'),'Tenant',to_date('25-09-12','DD-MM-RR'),3,null,'TANANT',to_date('14-09-12','DD-MM-RR')
,to_date('31-03-20','DD-MM-RR'),null,1,1,to_date('25-09-12','DD-MM-RR'));

--Master date for egpt_property_usage_master
delete from egpt_mstr_bndry_category;
delete from egpt_mstr_category;
delete from egpt_property_usage_master;
INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Residence',now(),4,'Residential','RESD',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());
INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Shops',now(),4,'Shops','SHOP',2,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());

INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Office/banks',now(),4,'Office/banks','OFFICE',3,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());

INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Hospitals/ Nursing Homes',now(),4,'Hospitals/ Nursing Homes','Hospital',4,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());

INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Educational Institutions',now(),4,'Educational Institutions','EDUCATION',3,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());

INSERT INTO egpt_property_usage_master(id,usg_name,modified_date,usage_factor,usg_name_local,code,order_id,from_date,to_date,is_enabled,created_by,modified_by,created_date) values
(nextval('SEQ_EGPT_PROPERTY_USAGE_MASTER'),'Hotels/lodges/ Restarants',now(),4,'Hotels/lodges/ Restarants','HOTEL',3,to_date('01-04-11','DD-MM-RR'),to_date('31-03-20','DD-MM-RR'),1,1,1,now());

delete from egpt_struc_cl;
--egpt_struc_cl
Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'Rcc Posh','Rcc Posh',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'R-P',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'Rcc Ordinary','Rcc Ordinary',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'R-O',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'M-terras','M-terras',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'M-T',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'M-tiled/ac Sheet/gi Roof','M-tiled/ac Sheet/gi Roof',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'M-TSR',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'Country Tiled','Country Tiled',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'C-T',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

Insert into egpt_struc_cl (ID,CONSTR_NUM,CONSTR_TYPE,CONSTR_DESCR,CONSTR_FACTOR,MODIFIED_DATE,ID_INSTALLMENT,IS_HISTORY,CREATED_BY,FLOOR_NUM,CODE,ORDER_ID,FROM_DATE,TO_DATE,CREATED_DATE,MODIFIED_BY) values (nextval('SEQ_EGPT_STRUC_CL'),1,'Huts','Huts',1.25,CURRENT_TIMESTAMP,129,'N',1,1,'H',1,to_date('01-04-11','DD-MM-RR'),to_date('31-03-08','DD-MM-RR'),CURRENT_TIMESTAMP,1);

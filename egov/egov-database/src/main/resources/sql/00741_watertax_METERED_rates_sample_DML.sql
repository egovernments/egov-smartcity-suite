
insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='DOMESTIC'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='DOMESTIC') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);


insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

Insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='COMMERCIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='COMMERCIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);


insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='APARTMENT'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='APARTMENT') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='HOTEL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='HOTEL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='INDUSTRIAL'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='INDUSTRIAL') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='SURFACEWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='SURFACEWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1/2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1/2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3/4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3/4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='1 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='1 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='2 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='2 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='3 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='3 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

insert into egwtr_water_rates_header  values (nextval('seq_egwtr_water_rates_header'),'METERED',
(select id from egwtr_usage_type where code='LODGES'),
(select id from egwtr_water_source where code='GROUNDWATER'),
(select id from egwtr_pipesize where code='4 Inch'),true,now(),now(),1,1,0);

insert into egwtr_water_rates_details values (nextval('seq_egwtr_water_rates_details'),
(select id from egwtr_water_rates_header where connectiontype='METERED' and 
usagetype=(select id from egwtr_usage_type where code='LODGES') 
and watersource=(select id from egwtr_water_source where code='GROUNDWATER') 
and pipesize =(select id from egwtr_pipesize where code='4 Inch')),
0,100000,20,null,null,now(),current_date+200,0);

--rollback delete from egwtr_water_rates_details where waterratesheader in (select id from egwtr_water_rates_header where connectiontype='METERED');
--rollback delete from egwtr_water_rates_header where connectiontype='METERED';
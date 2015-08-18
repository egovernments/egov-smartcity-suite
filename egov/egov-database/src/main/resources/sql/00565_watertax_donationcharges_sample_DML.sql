--1/4 inch to 1/2 inch
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL')),current_date-1,current_date+100,4000,0);



--1/2 inch to 3/4 inch

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1/2 Inch'),(select id from egwtr_pipesize where code='3/4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='1/2 Inch')),current_date-1,current_date+100,4000,0);



--3/4 inch to 1 inch



insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='3/4 Inch'),(select id from egwtr_pipesize where code='1 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='3/4 Inch')),current_date-1,current_date+100,4000,0);



--1 inch to 2 inch


insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_pipesize where code='2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='1 Inch')),current_date-1,current_date+100,4000,0);



--2 inch to 3 inch


insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='2 Inch'),(select id from egwtr_pipesize where code='3 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='2 Inch')),current_date-1,current_date+100,4000,0);



--3 inch to 4 inch


insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,1500,0);
--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,3500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='APARTMENT'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='APARTMENT') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='LODGES'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='LODGES') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

---
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='HOTEL'),(select id from egwtr_pipesize where code='3 Inch'),(select id from egwtr_pipesize where code='4 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='HOTEL') and minpipesize=(select id from egwtr_pipesize where code='3 Inch')),current_date-1,current_date+100,4000,0);

--rollback delete from egwtr_donation_details;
--rollback delete from egwtr_donation_header;




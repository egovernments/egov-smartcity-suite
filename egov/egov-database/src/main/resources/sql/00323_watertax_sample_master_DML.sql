

--connection charges

update egwtr_connectioncharges set todate=current_date+100;

--donation charges

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='COMMERCIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='COMMERCIAL')),current_date-1,current_date+100,1500,0);
--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DOMESTIC'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DOMESTIC')),current_date-1,current_date+100,2500,0);

--
insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='RESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='RESIDENTIAL')),current_date-1,current_date+100,3500,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='DRINKING'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='DRINKING')),current_date-1,current_date+100,1000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='DRINKING'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='DRINKING')),current_date-1,current_date+100,1000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='DRINKING'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='DRINKING')),current_date-1,current_date+100,1000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='DRINKING'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='DRINKING')),current_date-1,current_date+100,1000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='DRINKING'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='DRINKING')),current_date-1,current_date+100,1000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='INDUSTRIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='INDUSTRIAL')),current_date-1,current_date+100,4000,0);

--

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='BPL'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='BPL') and usagetype = (select id from egwtr_usage_type where code='NONRESIDENTIAL')),current_date-1,current_date+100,2000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='COMMERCIAL'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='COMMERCIAL') and usagetype = (select id from egwtr_usage_type where code='NONRESIDENTIAL')),current_date-1,current_date+100,2000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='DOMESTIC'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='DOMESTIC') and usagetype = (select id from egwtr_usage_type where code='NONRESIDENTIAL')),current_date-1,current_date+100,2000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='GENERAL'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='GENERAL') and usagetype = (select id from egwtr_usage_type where code='NONRESIDENTIAL')),current_date-1,current_date+100,2000,0);

insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,minsumpcapacity,maxsumpcapacity,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_donation_header'),(select id from egwtr_category where code='OYT'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),(select id from egwtr_pipesize where code='Zero Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),500,1000,true,now(),now(),1,1,0);

insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values (nextval('seq_egwtr_donation_details'),(select id from egwtr_donation_header where category=(select id from egwtr_category where code='OYT') and usagetype = (select id from egwtr_usage_type where code='NONRESIDENTIAL')),current_date-1,current_date+100,2000,0);


--security deposit

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='COMMERCIAL'),6,true,current_date-1,current_date+100,5000,now(),now(),1,1,0);

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='DOMESTIC'),6,true,current_date-1,current_date+100,3000,now(),now(),1,1,0);

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='RESIDENTIAL'),6,true,current_date-1,current_date+100,3500,now(),now(),1,1,0);

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='DRINKING'),6,true,current_date-1,current_date+100,2000,now(),now(),1,1,0);

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='INDUSTRIAL'),6,true,current_date-1,current_date+100,10000,now(),now(),1,1,0);

insert into egwtr_securitydeposit (id,usagetype,noofmonths,active,fromdate,todate,amount,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('seq_egwtr_securitydeposit'),(select id from egwtr_usage_type where code='NONRESIDENTIAL'),6,true,current_date-1,current_date+100,4500,now(),now(),1,1,0);


--Current installment master 

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),2015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'2015-16',null);

--demand reason master
INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date)
    VALUES (nextval('seq_eg_demand_reason_master'), 'WT_CONNECTION_CHARGES', (select id from eg_reason_category where code='TAX'),'N',
    (select id from eg_module where name = 'Water Tax Management'),'WTAXCONCHARGE', 1, now(), now());

INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date)
    VALUES (nextval('seq_eg_demand_reason_master'), 'WT_SECURITY_DEPOSIT', (select id from eg_reason_category where code='TAX'),'N',
    (select id from eg_module where name = 'Water Tax Management'), 'WTAXSECURITY',1, now(), now());


INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date)
    VALUES (nextval('seq_eg_demand_reason_master'), 'WT_DONATION_CHARGES', (select id from eg_reason_category where code='TAX'),'N',
    (select id from eg_module where name = 'Water Tax Management'),'WTAXDONATION', 1, now(), now());

--demand reason
insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CONNECTION_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management')
),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_SECURITY_DEPOSIT'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management')
),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_DONATION_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management')
),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));





--rollback delete from eg_demand_reason where id_installment=(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management');

--rollback delete from eg_demand_reason_master where module_id=(select id from eg_module where name = 'Water Tax Management');

--rollback delete from egwtr_securitydeposit;

--rollback delete from egwtr_donation_details

--rollback delete from egwtr_donation_header



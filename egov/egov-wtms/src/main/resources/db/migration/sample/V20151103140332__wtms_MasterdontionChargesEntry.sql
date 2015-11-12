
delete from  egwtr_donation_details where donationheader in (select id from egwtr_donation_header where category in(select id from egwtr_category where code='GENERAL')
and usagetype in(select id from egwtr_usage_type where code='COMMERCIAL') and
 minpipesize in(select id from egwtr_pipesize where code='1/4 Inch') and 
maxpipesize in(select id from egwtr_pipesize where code='1/2 Inch') and 
propertytype in(select id from egwtr_property_type where code='NON-RESIDENTIAL'));



delete from egwtr_donation_header where category in
(select id from egwtr_category where code='GENERAL')
and usagetype in(select id from egwtr_usage_type where code='COMMERCIAL') and
 minpipesize in(select id from egwtr_pipesize where code='1/4 Inch') and 
maxpipesize in(select id from egwtr_pipesize where code='1/2 Inch') and 
propertytype in(select id from egwtr_property_type where code='NON-RESIDENTIAL');

Insert into egwtr_donation_header (id,category,usagetype,minpipesize,maxpipesize,active,createddate
,lastmodifieddate,createdby,lastmodifiedby,version,propertytype)
 values (nextval('SEQ_EGWTR_DONATION_HEADER'),(select id from egwtr_category where code='GENERAL'),
 (select id from egwtr_usage_type where code='COMMERCIAL')
 ,(select id from egwtr_pipesize where code='1/4 Inch'),(select id from egwtr_pipesize where code='1/2 Inch'),'true',to_timestamp('2015-08-19 10:45:49.738717','null'),
 to_timestamp('2015-08-19 10:45:49.738717','null'),1,1,0,(select id from egwtr_property_type where code='NON-RESIDENTIAL'));
 
 Insert into egwtr_donation_details (id,donationheader,fromdate,todate,amount,version) values 
(nextval('SEQ_EGWTR_DONATION_DETAILS'),(select id from egwtr_donation_header where category in
(select id from egwtr_category where code='GENERAL')
and usagetype in(select id from egwtr_usage_type where code='COMMERCIAL') and
 minpipesize in(select id from egwtr_pipesize where code='1/4 Inch') and 
maxpipesize in(select id from egwtr_pipesize where code='1/2 Inch') and 
propertytype in(select id from egwtr_property_type where code='NON-RESIDENTIAL')),'2015-08-18','2016-11-27',1500.0,0);
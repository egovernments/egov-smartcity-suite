delete from egwtr_donation_details where donationheader in
(select id from egwtr_donation_header where propertytype in(select id from egwtr_property_type  where code='NON-RESIDENTIAL') 
and usagetype in(select id from egwtr_usage_type   where code='COMMERCIAL') and 
category in(select id from egwtr_category where code='GENERAL')and minpipesize 
in(select id  from egwtr_pipesize where code='1/4 Inch') and maxpipesize in(select id from egwtr_pipesize where code='1/4 Inch'));


delete from egwtr_donation_header where propertytype in(select id from egwtr_property_type  where code='NON-RESIDENTIAL') 
and usagetype in(select id from egwtr_usage_type   where code='COMMERCIAL') and 
category in(select id from egwtr_category where code='GENERAL')and minpipesize 
in(select id  from egwtr_pipesize where code='1/4 Inch') and maxpipesize in(select id from egwtr_pipesize where code='1/4 Inch');

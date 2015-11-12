------------------START------------------

alter table egadtax_rates_details alter column unitfrom SET DATA TYPE double precision; 
alter table egadtax_rates_details alter column  unitto  SET DATA TYPE double precision; 
alter table egadtax_rates_details alter column amount SET DATA TYPE double precision; 

alter table EGADTAX_AGENCYWISECOLLECTION_DETAIL alter column amount SET DATA TYPE double precision; 

alter table egadtax_hoarding alter column length SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column width SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column breadth SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column totalheight SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column taxamount SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column encroachmentfee SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column pendingTax SET DATA TYPE double precision; 
alter table egadtax_hoarding alter column measurement SET DATA TYPE double precision; 


------------------END------------------
------------------START------------------
update egadtax_hoardingdocument_type set version=0 where version is null;
-----------------END------------------
------------------START------------------

ALTER SEQUENCE SEQ_EGADTAX_APPLICATION RENAME TO SEQ_EGADTAX_PERMITDETAILS;
ALTER TABLE EGADTAX_APPLICATION RENAME TO EGADTAX_PERMITDETAILS;
alter table EGADTAX_PERMITDETAILS alter column permissionnumber DROP not null;  
alter table EGADTAX_PERMITDETAILS add column isActive boolean DEFAULT true;
alter table  egadtax_advertisement rename column  agency to currentAgency;


alter table EGADTAX_PERMITDETAILS add column agency bigint ;
alter table EGADTAX_PERMITDETAILS add column advertiser character varying(125);
alter table EGADTAX_PERMITDETAILS add column advertisementparticular character varying(512);
alter table EGADTAX_PERMITDETAILS add column  ownerdetail character varying(125) ;

alter table EGADTAX_advertisement drop column currentAgency ;
alter table EGADTAX_advertisement drop column advertiser;
alter table EGADTAX_advertisement drop column advertisementparticular;
alter table EGADTAX_advertisement drop column  ownerdetail;

alter table EGADTAX_advertisement drop column currentTaxAmount ;
alter table EGADTAX_advertisement drop column currentEncroachmentFee;

alter table EGADTAX_PERMITDETAILS add column measurement double precision NOT NULL;
 alter table EGADTAX_PERMITDETAILS add column unitofmeasure bigint NOT NULL;
 alter table EGADTAX_PERMITDETAILS add column length double precision;
 alter table EGADTAX_PERMITDETAILS add column width double precision;
 alter table EGADTAX_PERMITDETAILS add column breadth double precision;
 alter table EGADTAX_PERMITDETAILS add column totalheight double precision;


alter table EGADTAX_advertisement drop column measurement ;
alter table EGADTAX_advertisement drop column unitofmeasure;
alter table EGADTAX_advertisement drop column length;
alter table EGADTAX_advertisement drop column  width;
alter table EGADTAX_advertisement drop column  breadth;
alter table EGADTAX_advertisement drop column  totalheight;

alter table egadtax_hoarding_docs  rename column hoarding to advertisement;
alter table egadtax_hoarding_docs rename to egadtax_advertisement_docs;

ALTER TABLE ONLY egadtax_permitdetails
    ADD CONSTRAINT pk_adtax_permitdetails PRIMARY KEY (id);

 ALTER TABLE egadtax_permitdetails ADD CONSTRAINT fk_adtax_permitdetails_parent FOREIGN KEY (previousapplicationid) 
    REFERENCES egadtax_permitdetails(id);

-----------------END------------------
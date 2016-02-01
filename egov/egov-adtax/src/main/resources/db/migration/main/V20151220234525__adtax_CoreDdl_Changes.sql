------------------START------------------


ALTER TABLE egadtax_hoarding RENAME TO egadtax_advertisement;
ALTER SEQUENCE SEQ_EGADTAX_HOARDING RENAME TO SEQ_EGADTAX_ADVERTISEMENT;



delete from egadtax_hoarding_docs;
delete from egadtax_document_files;
delete from egadtax_advertisement;
delete from egadtax_hoardingdocument;

alter table  egadtax_advertisement drop column applicationnumber;
alter table  egadtax_advertisement drop column applicationdate;
alter table  egadtax_advertisement drop column permissionnumber;
alter table  egadtax_advertisement rename column hoardingnumber to advertisementnumber;
alter table  egadtax_advertisement drop column hoardingname;
alter table  egadtax_advertisement drop column advertisementduration;
alter table  egadtax_advertisement rename column  taxamount to currentTaxAmount;
alter table  egadtax_advertisement rename column  encroachmentfee to currentEncroachmentFee;
alter table  egadtax_advertisement drop column pendingtax;



create sequence SEQ_EGADTAX_APPLICATION;


CREATE TABLE EGADTAX_APPLICATION (
    id bigint NOT NULL,
    advertisement bigint not null,
    applicationnumber character varying(25) NOT NULL,
    applicationdate timestamp without time zone NOT NULL,
    permissionnumber character varying(25) NOT NULL,
    status bigint NOT NULL,
    advertisementduration character varying(25) NOT NULL,
    taxamount bigint NOT NULL,
    encroachmentfee bigint,
    permissionstartdate  timestamp without time zone not null,
    permissionenddate  timestamp without time zone not null,
    version bigint DEFAULT 0,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    previousapplicationid bigint 
);


-----------------END------------------
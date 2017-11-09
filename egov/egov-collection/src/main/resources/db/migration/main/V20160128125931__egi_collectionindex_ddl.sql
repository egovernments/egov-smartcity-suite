------------------START------------------
CREATE TABLE eg_collectionindex (
    id bigint NOT NULL,
    receiptdate date NOT NULL,
    receiptnumber character varying(50) NOT NULL,
    billingservice character varying(100) NOT NULL,
    paymentmode character varying(50) NOT NULL,
    arrearamount double precision,
    penaltyamount double precision,
    currentamount double precision,
    advanceamount double precision,
    totalamount double precision NOT NULL,
    channel character varying(50) NOT NULL, 
    paymentgateway character varying(100),
    billnumber bigint,
    consumercode character varying(50) NOT NULL,
    ulbname character varying(250) NOT NULL,
    districtname character varying(100) NOT NULL,
    regionname character varying(100) NOT NULL,
    status character varying(50) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric NOT NULL
);
CREATE SEQUENCE seq_eg_collectionindex
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_collectionindex ADD CONSTRAINT eg_collectionindex_pkey PRIMARY KEY (id);
-------------------END-------------------

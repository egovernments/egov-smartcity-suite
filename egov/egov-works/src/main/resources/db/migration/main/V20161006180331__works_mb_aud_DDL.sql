CREATE TABLE egw_mb_header_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  mb_refno character varying(100) ,
  mb_date timestamp without time zone ,
  from_page_no bigint ,
  to_page_no bigint,
  abstract character varying(1024) ,
  contractor_comments character varying(1024),  
  status_id bigint,
  is_legacy_mb boolean,
  mb_amount double precision,
  approved_date timestamp without time zone,
  MB_ISSUED_DATE timestamp without time zone,
  CANCELLATIONREASON varchar(50),
  CANCELLATIONREMARKS varchar(126),
  createdby bigint ,
  lastmodifiedby bigint,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  version bigint,
  revtype numeric
);

ALTER TABLE ONLY egw_mb_header_aud ADD CONSTRAINT pk_egw_mb_header_aud PRIMARY KEY (id, rev);


CREATE TABLE egw_mb_details_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  mbheader_id bigint,
  quantity double precision,
  rate double precision,
  amount double precision,
  remarks character varying(1024),
  order_number character varying(100),
  order_date timestamp without time zone,
  createdby bigint ,
  lastmodifiedby bigint,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  version bigint,
  revtype numeric 
);

ALTER TABLE ONLY egw_mb_details_aud ADD CONSTRAINT pk_egw_mb_details_aud PRIMARY KEY (id, rev);


Create table EGW_MB_MEASUREMENTSHEET_AUD( 
  id bigint NOT NULL,
  rev integer NOT NULL,
  mbdetails bigint,
  no numeric,
  remarks varchar(1024),
  length numeric,
  width numeric,
  depthOrHeight numeric,
  quantity numeric,
  createddate timestamp without time zone,
  createdby bigint,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version bigint,
  revtype numeric 
);

ALTER TABLE ONLY EGW_MB_MEASUREMENTSHEET_AUD ADD CONSTRAINT PK_EGW_MB_MEASUREMENTSHEET_AUD PRIMARY KEY (id, rev);

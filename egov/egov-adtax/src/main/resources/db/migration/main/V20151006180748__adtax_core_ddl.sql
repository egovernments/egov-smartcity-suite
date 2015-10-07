------------------START------------------

CREATE TABLE egadtax_agency (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(250) NOT NULL,
    ssid character varying(50),
    emailid character varying(100),
    mobilenumber character varying(10) NOT NULL,
    address character varying(250),
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    version bigint DEFAULT 0,
    depositamount double precision NOT NULL,
    status character varying(50) NOT NULL
);
CREATE SEQUENCE SEQ_egadtax_AGENCY;
ALTER TABLE ONLY egadtax_agency
    ADD CONSTRAINT pk_adtax_agency PRIMARY KEY (id);

ALTER TABLE ONLY egadtax_agency
    ADD CONSTRAINT unq_adtax_agency_code UNIQUE (code);
ALTER TABLE ONLY egadtax_agency
    ADD CONSTRAINT unq_adtax_agency_name UNIQUE (name);

CREATE INDEX idx_adtax_agency_code ON egadtax_agency USING btree (code);
CREATE INDEX idx_adtax_agency_name ON egadtax_agency USING btree (name);


ALTER TABLE ONLY egadtax_agency
    ADD CONSTRAINT fk_adtax_agency_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egadtax_agency
    ADD CONSTRAINT fk_adtax_agency_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);

-------------------END-------------------
------------------START------------------


CREATE TABLE egadtax_category (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(250) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    createdby bigint NOT NULL,
    version bigint DEFAULT 0,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint
);
CREATE SEQUENCE SEQ_egadtax_CATEGORY;
ALTER TABLE ONLY egadtax_category
    ADD CONSTRAINT pk_adtax_category PRIMARY KEY (id);
ALTER TABLE ONLY egadtax_category
    ADD CONSTRAINT unq_adtax_category_code UNIQUE (code);
ALTER TABLE ONLY egadtax_category
    ADD CONSTRAINT unq_adtax_category_name UNIQUE (name);

ALTER TABLE ONLY egadtax_category
    ADD CONSTRAINT fk_adtax_category_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);

-------------------END-------------------
------------------START------------------

CREATE TABLE egadtax_document_files (
    document numeric,
    filestore numeric
);


-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_hoarding (
    id bigint NOT NULL,
    applicationnumber character varying(25) NOT NULL,
    applicationdate timestamp without time zone NOT NULL,
    permissionnumber character varying(25) NOT NULL,
    hoardingnumber character varying(25) NOT NULL,
    hoardingname character varying(125) NOT NULL,
    agency bigint NOT NULL,
    advertiser character varying(125) NOT NULL,
    advertisementparticular character varying(512) NOT NULL,
    propertytype bigint NOT NULL,
    propertynumber character varying(50),
    ownerdetail character varying(125) NOT NULL,
    category bigint NOT NULL,
    subcategory bigint NOT NULL,
    measurement bigint NOT NULL,
    unitofmeasure bigint NOT NULL,
    length bigint,
    width bigint,
    breadth bigint,
    totalheight bigint,
    class bigint,
    revenueinspector bigint NOT NULL,
    revenueboundary bigint,
    adminboundry bigint NOT NULL,
    address character varying(512) NOT NULL,
    advertisementduration character varying(25) NOT NULL,
    taxamount bigint NOT NULL,
    encroachmentfee bigint,
    demandid bigint,
    version bigint DEFAULT 0,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    status bigint NOT NULL,
    type bigint NOT NULL,
    longitude double precision,
    latitude double precision,
    legacy boolean DEFAULT false,
    penaltyCalculationDate  timestamp without time zone,
    pendingTax bigint DEFAULT 0
);
CREATE SEQUENCE SEQ_egadtax_hoarding;
ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT pk_adtax_hoarding PRIMARY KEY (id);


CREATE INDEX idx_adtax_hoarding_adminboundry ON egadtax_hoarding USING btree (adminboundry);
CREATE INDEX idx_adtax_hoarding_agency ON egadtax_hoarding USING btree (agency);
CREATE INDEX idx_adtax_hoarding_appnumber ON egadtax_hoarding USING btree (applicationnumber);
CREATE INDEX idx_adtax_hoarding_category ON egadtax_hoarding USING btree (category);
CREATE INDEX idx_adtax_hoarding_demandid ON egadtax_hoarding USING btree (demandid);
CREATE INDEX idx_adtax_hoarding_name ON egadtax_hoarding USING btree (hoardingname);
CREATE INDEX idx_adtax_hoarding_number ON egadtax_hoarding USING btree (hoardingnumber);

ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_adminboundry FOREIGN KEY (adminboundry) REFERENCES eg_boundary(id);

ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_agency FOREIGN KEY (agency) REFERENCES egadtax_agency(id);
ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_category FOREIGN KEY (category) REFERENCES egadtax_category(id);
ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_demand FOREIGN KEY (demandid) REFERENCES eg_demand(id);
ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_revenueboundary FOREIGN KEY (revenueboundary) REFERENCES eg_boundary(id);
-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_hoarding_docs (
    hoarding bigint,
    document bigint
);

-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_hoardingdocument (
    id numeric NOT NULL,
    doctype numeric,
    description character varying(100),
    docdate date,
    enclosed boolean,
    createddate timestamp without time zone,
    createdby numeric,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby numeric,
    version numeric DEFAULT 0
);

create sequence SEQ_egadtax_document;
ALTER TABLE ONLY egadtax_hoardingdocument
    ADD CONSTRAINT egadtax_document_pkey PRIMARY KEY (id);
-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_hoardingdocument_type (
    id numeric NOT NULL,
    name character varying(100),
    mandatory boolean,
    version numeric DEFAULT 0
);
create sequence SEQ_egadtax_document_TYPE;
ALTER TABLE ONLY egadtax_hoardingdocument_type
    ADD CONSTRAINT egadtax_document_type_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egadtax_hoardingdocument
    ADD CONSTRAINT fk_adtax_document_doctype FOREIGN KEY (doctype) REFERENCES egadtax_hoardingdocument_type(id);
-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_rates (
    id bigint NOT NULL,
    category bigint NOT NULL,
    subcategory bigint NOT NULL,
    unitofmeasure bigint NOT NULL,
    active boolean DEFAULT true NOT NULL,
    validfromdate timestamp without time zone DEFAULT ('now'::text)::date,
    validtodate timestamp without time zone,
    version bigint DEFAULT 0,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    class bigint NOT NULL
);
CREATE SEQUENCE SEQ_egadtax_rates;
ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT pk_adtax_rates PRIMARY KEY (id);

ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT unq_adtax_rates_cat_subcat_uom_class UNIQUE (category, subcategory, unitofmeasure, class);
CREATE INDEX idx_adtax_rates_category ON egadtax_rates USING btree (category);

CREATE INDEX idx_adtax_rates_subcategory ON egadtax_rates USING btree (subcategory);
CREATE INDEX idx_adtax_rates_unitofmeasure ON egadtax_rates USING btree (unitofmeasure);
ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT fk_adtax_rates_category FOREIGN KEY (category) REFERENCES egadtax_category(id);
-------------------END-------------------
------------------START------------------

CREATE TABLE egadtax_rates_class (
    id bigint NOT NULL,
    description character varying(50) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    version bigint DEFAULT 0
);
CREATE SEQUENCE SEQ_egadtax_ratesClass;

ALTER TABLE ONLY egadtax_rates_class
    ADD CONSTRAINT pk_adtax_ratesclass PRIMARY KEY (id);

ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT fk_adtax_rates_class FOREIGN KEY (class) REFERENCES egadtax_rates_class(id);

ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_class FOREIGN KEY (class) REFERENCES egadtax_rates_class(id);
-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_rates_details (
    id bigint NOT NULL,
    unitfrom bigint DEFAULT 0 NOT NULL,
    unitto bigint DEFAULT 1 NOT NULL,
    rate bigint NOT NULL,
    version bigint DEFAULT 0,
    amount bigint DEFAULT 0 NOT NULL
);
CREATE SEQUENCE SEQ_egadtax_ratesdetails;

ALTER TABLE ONLY egadtax_rates_details
    ADD CONSTRAINT pk_adtax_ratesdetails PRIMARY KEY (id);

CREATE INDEX idx_adtax_ratedtl_unitfrom ON egadtax_rates_details USING btree (unitfrom);
CREATE INDEX idx_adtax_ratedtl_unitto ON egadtax_rates_details USING btree (unitto);

ALTER TABLE ONLY egadtax_rates_details
    ADD CONSTRAINT fk_adtax_ratesdetail_rate FOREIGN KEY (rate) REFERENCES egadtax_rates(id);
-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_revenueinspectors (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    version bigint DEFAULT 0
);
CREATE SEQUENCE SEQ_egadtax_revenueinspectors;

ALTER TABLE ONLY egadtax_revenueinspectors
    ADD CONSTRAINT pk_adtax_ri PRIMARY KEY (id);

ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_revenueinsptor FOREIGN KEY (revenueinspector) REFERENCES egadtax_revenueinspectors(id);

-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_subcategory (
    id bigint NOT NULL,
    category bigint NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(512) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    createdby bigint NOT NULL,
    version bigint DEFAULT 0,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint
);
CREATE SEQUENCE SEQ_egadtax_SUBCATEGORY;
ALTER TABLE ONLY egadtax_subcategory
    ADD CONSTRAINT pk_adtax_subcategory PRIMARY KEY (id);
ALTER TABLE ONLY egadtax_subcategory
    ADD CONSTRAINT unq_adtax_subcategory_code UNIQUE (code);
ALTER TABLE ONLY egadtax_subcategory
    ADD CONSTRAINT unq_adtax_subcategory_description UNIQUE (description);

ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_subcategory FOREIGN KEY (subcategory) REFERENCES egadtax_subcategory(id);

ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT fk_adtax_rates_subcategory FOREIGN KEY (subcategory) REFERENCES egadtax_subcategory(id);
ALTER TABLE ONLY egadtax_subcategory
    ADD CONSTRAINT fk_adtax_subcategory_category FOREIGN KEY (category) REFERENCES egadtax_category(id);

ALTER TABLE ONLY egadtax_subcategory
    ADD CONSTRAINT fk_adtax_subcategory_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);

-------------------END-------------------
------------------START------------------
CREATE TABLE egadtax_unitofmeasure (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(50) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    version bigint DEFAULT 0,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint
);
CREATE SEQUENCE SEQ_egadtax_UnitOfMeasure;
ALTER TABLE ONLY egadtax_unitofmeasure
    ADD CONSTRAINT pk_adtax_unitofmeasure PRIMARY KEY (id);

CREATE SEQUENCE SEQ_egadtax_propertytype;
ALTER TABLE ONLY egadtax_hoarding
    ADD CONSTRAINT fk_adtax_hoarding_uom FOREIGN KEY (unitofmeasure) REFERENCES egadtax_unitofmeasure(id);


ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT fk_adtax_rates_uom FOREIGN KEY (unitofmeasure) REFERENCES egadtax_unitofmeasure(id);



ALTER TABLE ONLY egadtax_unitofmeasure
    ADD CONSTRAINT fk_adtax_unitofmeasure_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);

create sequence SEQ_advertisementbill_NUMBER;
-------------------END-------------------




































----Donation Header master---

CREATE TABLE egwtr_donation_header_aud(
id integer NOT NULL,
rev integer NOT NULL,
category bigint,
usagetype bigint  ,
propertytype bigint,
minpipesize bigint,
maxpipesize bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);


ALTER TABLE ONLY egwtr_donation_header_aud ADD CONSTRAINT pk_egwtr_donation_header_aud PRIMARY KEY (id, rev);

------Donation Details master----

CREATE TABLE egwtr_donation_details_aud(
id integer NOT NULL,
rev integer NOT NULL,
donationheader bigint,
fromdate date  ,
todate date,
amount double precision,
revtype numeric
);


ALTER TABLE ONLY egwtr_donation_details_aud ADD CONSTRAINT pk_egwtr_donation_details_aud PRIMARY KEY (id, rev);

-----Water Rates  Header master-------

CREATE TABLE egwtr_water_rates_header_aud(
id integer NOT NULL,
rev integer NOT NULL,
connectiontype character varying(50),
usagetype bigint  ,
watersource bigint,
pipesize bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_water_rates_header_aud ADD CONSTRAINT pk_egwtr_water_rates_header_aud PRIMARY KEY (id, rev);


-----Water Rates Details master------

CREATE TABLE egwtr_water_rates_details_aud(
id integer NOT NULL,
rev integer NOT NULL,
waterratesheader bigint,
monthlyrate double precision,
fromdate date  ,
todate date,
revtype numeric
);


ALTER TABLE ONLY egwtr_water_rates_details_aud ADD CONSTRAINT pk_egwtr_water_rates_details_aud PRIMARY KEY (id, rev);


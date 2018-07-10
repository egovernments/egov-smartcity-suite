

--------------START------------------
CREATE TABLE egwtr_connection_address
(
id bigint NOT NULL,
connectiondetailsid bigint NOT NULL,
zone bigint,
adminWard bigint,
revenueWard bigint,
locality bigint,
ownername character varying(50),
address character varying(250),
doornumber character varying(50),
createddate timestamp without time zone NOT NULL,
lastmodifieddate timestamp without time zone,
createdby bigint NOT NULL,
lastmodifiedby bigint,
version numeric default 0,
CONSTRAINT pk_egwtr_connection_address_id PRIMARY KEY (id),
CONSTRAINT fk_egwtr_connection_details_conndtls FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_egwtr_connection_details_zone FOREIGN KEY (zone) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_egwtr_connection_details_adminWard FOREIGN KEY (adminWard) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_egwtr_connection_details_revenueWard FOREIGN KEY (revenueWard) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_egwtr_connection_details_locality FOREIGN KEY (locality) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE seq_egwtr_connection_address;

--------------END-----------------
--------------START------------------

CREATE TABLE egwtr_connection_address_aud 
(
id integer NOT NULL,
rev integer NOT NULL,
connectiondetailsid bigint,
zone bigint,
adminWard bigint,
revenueWard bigint,
locality bigint,
ownername character varying(50),
address character varying(250),
doornumber character varying(50),
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_connection_address_aud ADD CONSTRAINT pk_egwtr_connection_address_aud PRIMARY KEY (id,rev);

--------------END-----------------

ALTER TABLE egwtr_connectiondetails ADD COLUMN communicationaddress character varying(1024);


CREATE TABLE egwtr_donation_master
(
 id bigint NOT NULL,
 propertytype bigint NOT NULL,
 categorytype bigint NOT NULL,
 usagetype bigint NOT NULL,
 maxpipesize bigint NOT NULL,
 minpipesize bigint NOT NULL,
 donationamount double precision,
 active boolean NOT NULL,
 effectivedate  date NOT NULL,
 createddate timestamp without time zone NOT NULL,
 lastmodifieddate timestamp without time zone,
 createdby bigint NOT NULL,
 lastmodifiedby bigint,
 version numeric DEFAULT 0,
 CONSTRAINT fk_donation_property_type_fkey FOREIGN KEY (propertytype) REFERENCES egwtr_property_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_donation_pipe_max_size_fkey FOREIGN KEY (maxpipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_donation_pipe_min_size_fkey FOREIGN KEY (minpipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_donation_category_type_fkey FOREIGN KEY (categorytype)  REFERENCES egwtr_category (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_donation_usage_type_fkey FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_donation_master;
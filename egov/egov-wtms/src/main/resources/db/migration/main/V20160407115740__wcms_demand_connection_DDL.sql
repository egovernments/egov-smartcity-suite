CREATE TABLE egwtr_demand_connection
(
  id bigint NOT NULL,
  demand bigint NOT NULL,
  connectiondetails bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_demand_connection_pkey PRIMARY KEY (id),
  CONSTRAINT fk_demand_connection_fkey FOREIGN KEY (demand) REFERENCES eg_demand (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_demand_connection_detailsid_fkey FOREIGN KEY (connectiondetails) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO    ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE seq_egwtr_demand_connection;

INSERT INTO egwtr_demand_connection (id ,demand,connectiondetails,createdby,createddate,lastmodifieddate,lastmodifiedby,version)
SELECT  nextval('seq_egwtr_demand_connection')
 ,demand,id,createdby,now(),now(),lastmodifiedby,version
FROM egwtr_connectiondetails where demand IS NOT NULL;


DROP VIEW IF EXISTS egwtr_mv_dcb_view;

ALTER TABLE egwtr_connectiondetails drop column demand;

 




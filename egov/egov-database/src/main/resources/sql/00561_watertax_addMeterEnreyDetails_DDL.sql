
CREATE TABLE egwtr_meter_connection_details
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  currentreading bigint,
  currentreadingdate date,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_meter_connection_details PRIMARY KEY (id),
  CONSTRAINT fk_meter_connection_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id) ,
  CONSTRAINT fk_meter_connection_details FOREIGN KEY (connectiondetailsid)
      REFERENCES egwtr_connectiondetails (id) ,
  CONSTRAINT fk_meter_connection_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id) 
);



create sequence seq_egwtr_meter_connection_details;

ALTER TABLE egwtr_water_rates_details add COLUMN version BIGINT;
CREATE TABLE egswtax_demand_connection
(
  id bigint NOT NULL,
  demand bigint NOT NULL,
  connection bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL
);

ALTER TABLE ONLY egswtax_demand_connection
    ADD CONSTRAINT pk_egswtax_demand_connection_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_demand_connection
    ADD CONSTRAINT fk_demand_connection_id_fkey FOREIGN KEY (connection) REFERENCES egswtax_connection (id);
ALTER TABLE ONLY egswtax_demand_connection
    ADD CONSTRAINT fk_demand_connection_fkey FOREIGN KEY (demand) REFERENCES eg_demand (id);

CREATE SEQUENCE seq_egswtax_demand_connection; 


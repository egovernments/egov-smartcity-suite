----------------------------------------------START-------------------------------------------
CREATE TABLE egasset_locationdetails
(
  id bigint NOT NULL,
  location_id bigint NOT NULL,
  zone_id bigint,
  revenue_ward_id bigint,
  block_id bigint,
  street_id bigint,
  election_ward_id bigint,
  doornumber character varying(256),
  pincode bigint,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  version numeric,
  CONSTRAINT pk_location PRIMARY KEY (id),
  CONSTRAINT fk_location FOREIGN KEY (location_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_location_zone FOREIGN KEY (zone_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_boundary_revenue_ward FOREIGN KEY (revenue_ward_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_boundary_block FOREIGN KEY (block_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_boundary_street FOREIGN KEY (street_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_boundary_election_ward FOREIGN KEY (election_ward_id)
      REFERENCES eg_boundary (id)
);

CREATE SEQUENCE seq_egasset_locationdetails;
---------------------------------------------- END -------------------------------------------

--rollback  DROP SEQUENCE seq_egasset_locationdetails;
--rollback  DROP TABLE egasset_locationdetails;
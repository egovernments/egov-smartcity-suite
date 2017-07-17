
CREATE SEQUENCE seq_egcl_service_instrumentaccounts;

CREATE TABLE egcl_service_instrumentaccounts
(
  id bigint NOT NULL,
  instrumenttype bigint,
  servicedetails bigint NOT NULL,
  chartofaccounts bigint NOT NULL,
  createdBy bigint NOT NULL,
  createdDate timestamp without time zone NOT NULL,
  lastModifiedBy bigint NOT NULL,
  lastModifiedDate timestamp without time zone NOT NULL,
  CONSTRAINT pk_egcl_service_instrumentaccounts PRIMARY KEY (id),
  CONSTRAINT fk_egcl_payment_service FOREIGN KEY (servicedetails)
      REFERENCES egcl_servicedetails (id),
  CONSTRAINT fk_egcl_service_coa FOREIGN KEY (chartofaccounts)
      REFERENCES chartofaccounts (id),
 CONSTRAINT fk_egcl_service_instrtype FOREIGN KEY (instrumenttype)
      REFERENCES egf_instrumenttype (id)
);

CREATE INDEX idx_servicedetails_instracc ON egcl_service_instrumentaccounts (servicedetails);
CREATE INDEX idx_servicedetails_coa ON egcl_service_instrumentaccounts (chartofaccounts);
CREATE INDEX idx_servicedetails_instrtype ON egcl_service_instrumentaccounts (instrumenttype);

COMMENT ON COLUMN egcl_service_instrumentaccounts.servicedetails IS 'Servicedetails with type P will be mapped';

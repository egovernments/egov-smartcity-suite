alter table EGCL_SERVICE_INSTRUMENTACCOUNTS add column billservicedetails bigint;
alter table EGCL_SERVICE_INSTRUMENTACCOUNTS add constraint fk_egcl_instrac_billservice FOREIGN KEY (billservicedetails) REFERENCES egcl_servicedetails (id);
CREATE INDEX idx_instracc_billservice ON egcl_service_instrumentaccounts (billservicedetails);
COMMENT ON COLUMN EGCL_SERVICE_INSTRUMENTACCOUNTS.billservicedetails IS 'Bill Servicedetails with type B will be mapped';
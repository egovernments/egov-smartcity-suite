CREATE TABLE egtl_penaltyrates (
    id bigint NOT NULL,
    fromrange bigint NOT NULL,
    torange bigint NOT NULL,
    rate double precision NOT NULL DEFAULT 0,
    licenseAppType bigint,
    version bigint DEFAULT 0,
  CONSTRAINT pk_egtl_penaltyrates PRIMARY KEY (id)
);

ALTER TABLE ONLY egtl_penaltyrates
    ADD CONSTRAINT fk_egtl_licenseAppType FOREIGN KEY (licenseAppType) REFERENCES egtl_mstr_app_type(id);

CREATE UNIQUE INDEX idx_egtl_penaltyrates ON egtl_penaltyrates USING btree (id);

CREATE SEQUENCE seq_egtl_penaltyrates;

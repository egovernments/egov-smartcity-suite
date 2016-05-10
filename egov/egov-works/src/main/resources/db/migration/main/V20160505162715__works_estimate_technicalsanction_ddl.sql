CREATE TABLE egw_estimate_technicalsanction
(
  id bigint NOT NULL,
  abstractestimate bigint NOT NULL, 
  technicalsanctionnumber character varying(50),
  technicalsanctiondate timestamp without time zone,
  technicalsanctionby bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,  
  CONSTRAINT pk_estimate_technicalsanction PRIMARY KEY (id),
  CONSTRAINT fk_estimate_technicalsanction_abstractestimate FOREIGN KEY (abstractestimate)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_estimate_technicalsanction_technicalsanctionby FOREIGN KEY (technicalsanctionby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_estimate_technicalsanction_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_estimate_technicalsanction_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_estimate_technicalsanction_abstractestimate ON egw_estimate_technicalsanction USING btree (abstractestimate);

CREATE INDEX idx_estimate_technicalsanction_technicalsanctionby ON egw_estimate_technicalsanction USING btree (technicalsanctionby);

CREATE SEQUENCE seq_egw_estimate_technicalsanction;

--rollback drop sequence seq_egw_estimate_technicalsanction;
--rollback drop table egw_estimate_technicalsanction;
CREATE TABLE egw_abstractestimate_deduction
(
  id bigint NOT NULL,
  abstractestimate bigint NOT NULL,
  chartofaccounts bigint NOT NULL,
  percentage double precision,
  amount varchar(20) NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,
  CONSTRAINT pk_abstractestimatededuction PRIMARY KEY (id),
  CONSTRAINT fk_abstractestimate_id FOREIGN KEY (abstractestimate)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_chartofaccounts_id FOREIGN KEY (chartofaccounts)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_abstractestimatededuction_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_abstractestimatededuction_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_abstractestimatededuction_abstractestimate ON egw_abstractestimate_deduction USING btree (abstractestimate);
CREATE INDEX idx_abstractestimatededuction_chartofaccounts ON egw_abstractestimate_deduction USING btree (chartofaccounts);

CREATE SEQUENCE SEQ_EGW_ABSTRACTESTIMATE_DEDUCTION;

--rollback DROP SEQUENCE SEQ_EGW_ABSTRACTESTIMATE_DEDUCTION;
--rollback DROP TABLE egw_abstractestimate_deduction;


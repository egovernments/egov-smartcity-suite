-----------------START----------------------
CREATE TABLE egw_lineestimate_appropriation
(
  id bigint NOT NULL,
  lineestimatedetails bigint NOT NULL,
  budgetusage bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,
  CONSTRAINT pk_lineestimate_appropriation PRIMARY KEY (id),
  CONSTRAINT fk_lineestimate_appropriation_lineestimatedetails FOREIGN KEY (lineestimatedetails)
      REFERENCES egw_lineestimate_details (id),
  CONSTRAINT fk_lineestimate_appropriation_budgetusage FOREIGN KEY (budgetusage)
      REFERENCES egf_budget_usage (id),
  CONSTRAINT fk_lineestimate_appropriation_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
  CONSTRAINT fk_lineestimate_appropriation_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id)
);

CREATE INDEX idx_lineestimate_appropriation_lineestimatedetails ON egw_lineestimate_appropriation USING btree (lineestimatedetails);
CREATE INDEX idx_lineestimate_appropriation_budgetusage ON egw_lineestimate_appropriation USING btree (budgetusage);
CREATE INDEX idx_lineestimate_appropriation_createdby ON egw_lineestimate_appropriation USING btree (createdby);
CREATE INDEX idx_lineestimate_appropriation_lastmodifiedby ON egw_lineestimate_appropriation USING btree (lastmodifiedby);

CREATE SEQUENCE seq_egw_lineestimate_appropriation;


INSERT INTO eg_modules(id, name, description) VALUES (11, 'Works', 'Works Management');

--rollback drop table egw_lineestimate_appropriation;
--rollback delete from eg_modules where name = 'Works';
-----------------END----------------------------
alter table egmrs_registration drop column feecriteria;
alter table egmrs_registration add column feecriteria bigint;
ALTER TABLE egmrs_registration ADD CONSTRAINT fk_Mrs_regn_fee FOREIGN KEY (feecriteria) REFERENCES egmrs_fee(id);
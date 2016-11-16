CREATE TABLE egw_contractorbill_aud
(
  id bigint,
  rev integer,
  approveddate timestamp without time zone,
  billsequencenumber integer,
  approvedby bigint,
  revtype numeric
);

ALTER TABLE ONLY egw_contractorbill_aud ADD CONSTRAINT pk_egw_contractorbill_aud PRIMARY KEY (id, rev);

--rollback drop table egw_contractorbill_aud;
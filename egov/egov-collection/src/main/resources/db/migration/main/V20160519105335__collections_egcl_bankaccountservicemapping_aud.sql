CREATE TABLE egcl_bankaccountservicemapping_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  servicedetails bigint ,
  bankaccount bigint ,
  lastmodifiedby bigint ,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  revtype numeric
);

ALTER TABLE ONLY egcl_bankaccountservicemapping_aud ADD CONSTRAINT pk_egcl_bankaccountservicemapping_aud PRIMARY KEY (id, rev);


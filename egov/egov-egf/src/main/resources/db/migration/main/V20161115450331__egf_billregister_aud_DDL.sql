CREATE TABLE eg_billregister_aud
(
  id bigint,
  rev integer,
  billnumber character varying(50),
  billdate timestamp without time zone,
  billamount numeric(13,2),
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  statusid bigint,
  version bigint,
  revtype numeric
);

ALTER TABLE ONLY eg_billregister_aud ADD CONSTRAINT pk_eg_billregister_aud PRIMARY KEY (id, rev);

CREATE TABLE eg_billdetails_aud
(
  id bigint,
  rev integer,
  billid bigint,
  functionid bigint,
  glcodeid bigint,
  debitamount numeric(13,2),
  creditamount numeric(13,2),
  lastupdatedtime timestamp without time zone,
  narration character varying(250),
  version numeric DEFAULT 0,
  revtype numeric
);

ALTER TABLE ONLY eg_billdetails_aud ADD CONSTRAINT pk_eg_billdetails_aud PRIMARY KEY (id, rev);

CREATE TABLE eg_billpayeedetails_aud
(
  id bigint,
  rev integer,
  billdetailid bigint,
  accountdetailtypeid bigint,
  accountdetailkeyid bigint,
  debitamount numeric(13,2),
  creditamount numeric(13,2),
  lastupdatedtime timestamp without time zone,
  tdsid bigint,
  narration character varying(250),
  pc_department bigint,
  version numeric DEFAULT 0,
  revtype numeric
);

ALTER TABLE ONLY eg_billpayeedetails_aud ADD CONSTRAINT pk_eg_billpayeedetails_aud PRIMARY KEY (id, rev);

--rollback drop table eg_billpayeedetails_aud;
--rollback drop table eg_billdetails_aud;
--rollback drop table eg_billregister_aud;
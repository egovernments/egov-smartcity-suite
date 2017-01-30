-- DROP TABLE accountdetailtype_aud;

CREATE TABLE accountdetailtype_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  name character varying(50) NOT NULL,
  description character varying(50) NOT NULL,
  tablename character varying(50),
  columnname character varying(50),
  attributename character varying(50) NOT NULL,
  nbroflevels bigint NOT NULL,
  full_qualified_name character varying(250),
  createddate date,
  lastmodifieddate date,
  lastmodifiedby bigint,
  version bigint default 0,
  isactive boolean,
  revtype numeric
);

ALTER TABLE ONLY accountdetailtype_aud ADD CONSTRAINT pk_accountdetailtype_aud PRIMARY KEY (id, rev);

alter table accountdetailtype rename column created TO createddate;
alter table accountdetailtype rename column modifiedby TO lastmodifiedby;
alter table accountdetailtype rename column lastmodified TO lastmodifieddate;




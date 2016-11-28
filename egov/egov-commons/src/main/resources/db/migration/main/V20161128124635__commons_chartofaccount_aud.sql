-- DROP TABLE chartofaccounts_aud;

CREATE TABLE chartofaccounts_aud
(
  id bigint ,
  rev integer NOT NULL,
  glcode character varying(50) ,
  name character varying(150) ,
  description character varying(250),
  isactiveforposting boolean ,
  parentid bigint,
  purposeid bigint,
  operation character(1),
  type character(1) ,
  class smallint,
  classification smallint,
  functionreqd boolean,
  budgetcheckreq boolean,
  scheduleid bigint,
  receiptscheduleid bigint,
  receiptoperation character(1),
  paymentscheduleid bigint,
  paymentoperation character(1),
  majorcode character varying(255),
  fiescheduleid bigint,
  fieoperation character varying(1),
  createddate date,
  createdby bigint,
  lastmodifieddate date,
  lastmodifiedby bigint,
  version bigint default 0,
  revtype numeric 
);


ALTER TABLE ONLY chartofaccounts_aud ADD CONSTRAINT pk_chartofaccounts_aud PRIMARY KEY (id, rev);



-- DROP TABLE chartofaccountdetail_aud;

CREATE TABLE chartofaccountdetail_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  glcodeid bigint NOT NULL,
  detailtypeid bigint NOT NULL,
  createdby bigint,
  createddate date,
  lastmodifieddate date,
  lastmodifiedby bigint,
  version bigint default 0,
  revtype numeric 
);

ALTER TABLE ONLY chartofaccountdetail_aud ADD CONSTRAINT pk_chartofaccountdetail_aud PRIMARY KEY (id, rev);

alter table chartofaccounts add column version bigint default 0;
alter table chartofaccounts rename column created TO createddate;
alter table chartofaccounts rename column modifiedby TO lastmodifiedby;
alter table chartofaccounts rename column lastmodified TO lastmodifieddate;


alter table chartofaccountdetail add column version bigint default 0;
alter table chartofaccountdetail rename column modifiedby TO lastmodifiedby;
alter table chartofaccountdetail rename column modifieddate TO lastmodifieddate;


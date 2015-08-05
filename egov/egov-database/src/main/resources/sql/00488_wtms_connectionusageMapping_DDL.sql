CREATE TABLE egwtr_connectionusage
(
  id serial NOT NULL,
  usagetype bigint NOT NULL,
  connectiontype character varying(20),
  version numeric NOT NULL,
  CONSTRAINT pk_connectionusage_pkey PRIMARY KEY (id),
  CONSTRAINT fk_connectionusage_usageid_fkey FOREIGN KEY (usagetype)
      REFERENCES egwtr_usage_type (id)
     
);

create sequence SEQ_EGWTR_CONNECTIONUSAGE;
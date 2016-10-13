CREATE TABLE egmrs_fee_aud(
id integer NOT NULL,
rev integer NOT NULL,
criteria character varying(50) NOT NULL,
fees double precision,
fromdays  bigint,
todays bigint,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egmrs_fee_aud ADD CONSTRAINT pk_egmrs_fee_aud PRIMARY KEY (id, rev);

alter table egmrs_fee add column feeType smallint;

update egmrs_fee set feetype=1 where criteria ='Re-Issue Fee';

update egmrs_fee set feetype=0 where criteria !='Re-Issue Fee';
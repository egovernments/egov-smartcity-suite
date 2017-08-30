CREATE TABLE egpt_struc_cl_aud(
id integer NOT NULL,
rev integer NOT NULL,
constr_type character varying(128),
constr_descr character varying(512),
code character varying(16),
isactive boolean,
revtype numeric
);

ALTER TABLE ONLY egpt_struc_cl_aud ADD CONSTRAINT pk_egpt_struc_cl_aud PRIMARY KEY (id, rev);


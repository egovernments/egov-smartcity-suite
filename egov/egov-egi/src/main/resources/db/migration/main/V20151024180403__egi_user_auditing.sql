CREATE TABLE eg_user_aud(
id integer NOT NULL,
rev integer NOT NULL,
name character varying(100),
mobilenumber character varying(50),
emailid character varying(128),
revtype numeric
);

ALTER TABLE ONLY eg_user_aud ADD CONSTRAINT pk_eg_user_aud PRIMARY KEY (id, rev);
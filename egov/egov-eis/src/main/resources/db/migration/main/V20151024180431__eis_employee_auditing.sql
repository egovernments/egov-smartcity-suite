
CREATE TABLE egeis_employee_aud(
id integer NOT NULL,
rev integer NOT NULL
);

ALTER TABLE ONLY egeis_employee_aud ADD CONSTRAINT pk_egeis_employee_aud PRIMARY KEY (id, rev);
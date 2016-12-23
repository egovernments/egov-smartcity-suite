
----------columns added to egeis_employee_aud--------------------------------------
alter table egeis_employee_aud add code character varying (256);
alter table egeis_employee_aud add dateofappointment date;
alter table egeis_employee_aud add employeestatus character varying (16);
alter table egeis_employee_aud add employeetype bigint;
alter table egeis_employee_aud add dateofretirement date;



----------create audit table for egeis_assignment----------------------------------
 CREATE TABLE egeis_assignment_aud
(
  id integer not null,
  rev integer not null,
  position bigint,
  designation bigint,
  fund bigint,
  function bigint,
  department bigint,
  functionary bigint,
  grade bigint,
  fromdate date,
  todate date,
  isprimary boolean,
  employee bigint,
  revtype numeric,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egeis_assignment_aud ADD CONSTRAINT pk_egeis_assignment_aud PRIMARY KEY (id, rev);
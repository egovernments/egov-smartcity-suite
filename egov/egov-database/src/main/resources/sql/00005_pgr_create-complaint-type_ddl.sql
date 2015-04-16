CREATE TABLE pgr_complainttype
(
  id numeric NOT NULL,
  name character varying(150),
  dept_id numeric,
  days_to_resolve numeric,
  location_required bool,
  CONSTRAINT pk_pgr_complainttype_id PRIMARY KEY (id ),
  CONSTRAINT uk_pgr_complainttype_name UNIQUE (name),
  CONSTRAINT fk_pgr_complainttype_deptid FOREIGN KEY (dept_id) REFERENCES eg_department (id_dept)
);

--rollback DROP TABLE pgr_complainttype;

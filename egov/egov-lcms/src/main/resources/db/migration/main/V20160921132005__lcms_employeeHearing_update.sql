update eg_action set url='/ajax/getpositionEmployee' where contextroot='lcms' and url='/ajax/positions' and name='legalajaxforemployeeposition';


drop table eglc_employeehearing;

CREATE TABLE eglc_employeehearing
(
  id bigint NOT NULL,
  employee bigint,
  hearing bigint,
  version bigint,
  CONSTRAINT pk_emphearings PRIMARY KEY (id),
  CONSTRAINT fk_employeehearing_employee FOREIGN KEY (employee)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_employeehearing_hearing FOREIGN KEY (hearing)
  REFERENCES  eglc_hearings (id)
);
create sequence seq_egeis_deptdesig;

ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_design FOREIGN KEY (designation) REFERENCES eg_designation(id);

ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_departmnt FOREIGN KEY (department) REFERENCES eg_department(id);

ALTER TABLE ONLY eg_position
    ADD CONSTRAINT fk_egposition_deptdesig FOREIGN KEY (deptdesig) REFERENCES egeis_deptdesig(id);
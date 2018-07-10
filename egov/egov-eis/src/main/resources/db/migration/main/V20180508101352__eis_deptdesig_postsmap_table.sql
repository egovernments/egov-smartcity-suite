CREATE TABLE egeis_deptdesig_sanctionedposts (
    departmentid bigint NOT NULL,
    designationid bigint NOT NULL,
    sanctionedposts bigint NOT NULL,
    CONSTRAINT dept_fk FOREIGN KEY (departmentid) REFERENCES eg_department(id),
    CONSTRAINT desig_fk FOREIGN KEY (designationid) REFERENCES eg_designation(id),
    CONSTRAINT uq_deptdesig_sanctionedposts UNIQUE (departmentid,designationid)
);
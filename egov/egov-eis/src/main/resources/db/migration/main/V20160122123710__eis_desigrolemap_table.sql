CREATE TABLE egeis_desig_rolemapping (
    designationid bigint NOT NULL,
    roleid bigint NOT NULL,
    CONSTRAINT desig_fk FOREIGN KEY (designationid) REFERENCES eg_designation(id),
    CONSTRAINT role_fk FOREIGN KEY (roleid) REFERENCES eg_role(id),
    CONSTRAINT uq_desig_rolemapping UNIQUE (designationid,roleid)
    );

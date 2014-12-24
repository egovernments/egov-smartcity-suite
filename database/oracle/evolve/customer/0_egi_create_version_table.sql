#UP


CREATE TABLE egovcustomer_schema_versions (version varchar2(32) not null);

INSERT INTO  egovcustomer_schema_versions values ('-1');


#DOWN

DROP TABLE egovcustomer_schema_versions;


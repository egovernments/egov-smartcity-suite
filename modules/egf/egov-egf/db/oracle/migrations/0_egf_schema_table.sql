#UP
CREATE TABLE egf_schema_versions(version varchar2(32) not null);
INSERT INTO  egf_schema_versions values('-1');

#DOWN
DROP TABLE egf_schema_versions;

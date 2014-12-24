#UP
CREATE TABLE egpayroll_schema_versions(version varchar2(32) not null);
INSERT INTO  egpayroll_schema_versions values(-1);

#DOWN
DROP TABLE egpayroll_schema_versions;

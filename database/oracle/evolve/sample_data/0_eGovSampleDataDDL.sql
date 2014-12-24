#UP


CREATE TABLE eGovSampledata_schema_versions(version varchar2(32) not null);

INSERT INTO  eGovSampledata_schema_versions values ('-1');


#DOWN

DROP TABLE eGovSampledata_schema_versions;


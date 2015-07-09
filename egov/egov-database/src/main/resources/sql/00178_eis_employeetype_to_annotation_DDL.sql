CREATE SEQUENCE seq_egeis_employeetype;
CREATE TABLE egeis_employeetype (
id BIGINT CONSTRAINT PK_EGEIS_EMPLOYEETYPE_ID PRIMARY KEY,
name VARCHAR(256),
fromdate DATE,
todate DATE,
version BIGINT,
lastmodifieddate timestamp without time zone,
createddate timestamp without time zone,
createdby BIGINT,
lastmodifiedby BIGINT
);


--rollback DROP SEQUENCE seq_egeis_employeetype;
--rollback DROP TABLE egeis_employeetype;

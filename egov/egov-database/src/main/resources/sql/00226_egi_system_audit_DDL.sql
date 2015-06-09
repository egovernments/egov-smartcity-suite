CREATE TABLE EG_SYSTEMAUDIT(
id numeric PRIMARY KEY,
userid bigint,
ipAddress varchar(20),
userAgentInfo varchar(200),
loginTime timestamp,
logoutTime timestamp,
"version" numeric
);

CREATE SEQUENCE SEQ_EG_SYSTEMAUDIT;

DROP TABLE EG_LOGIN_LOG;

DROP SEQUENCE SEQ_EG_LOGIN_LOG;

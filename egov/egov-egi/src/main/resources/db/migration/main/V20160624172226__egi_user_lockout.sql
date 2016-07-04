CREATE SEQUENCE seq_eg_loginattempt;
CREATE TABLE eg_loginattempt(
id bigint primary key,
username varchar(150),
failedattempts numeric default 0,
lastmodifiedon timestamp without time zone,
version bigint default 0
);

ALTER TABLE eg_user add column accountlocked boolean default false;
CREATE TABLE EG_SYSTEMUSER(
id bigint primary key,
CONSTRAINT fk_systemuser_user FOREIGN KEY (id)
      REFERENCES eg_user (id)
);

UPDATE EG_USER SET "type" = 'SYSTEM' WHERE username='egovernments';

INSERT INTO EG_SYSTEMUSER VALUES ((select id from eg_user where username='egovernments')); 

UPDATE EG_USER SET "type" = 'SYSTEM' WHERE username='anonymous';

INSERT INTO EG_SYSTEMUSER VALUES ((select id from eg_user where username='anonymous')); 
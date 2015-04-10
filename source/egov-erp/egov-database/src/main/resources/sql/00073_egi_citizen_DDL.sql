drop table eg_citizen cascade;

create sequence seq_eg_citizen ;

SELECT setval('seq_eg_citizen', (select max(id) from eg_user)); 

CREATE TABLE eg_citizen
(
  id numeric NOT NULL,
  activationCode character varying(5)
);


--rollback DROP TABLE eg_citizen;
--rollback DROP SEQUENCE seq_eg_citizen;

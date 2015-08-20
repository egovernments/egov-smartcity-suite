
CREATE TABLE EG_CHAIRPERSON
(
  	ID 						bigint NOT NULL, 
    NAME					character varying(100) NOT NULL,
	FROMDATE				date NOT NULL,
	TODATE					date NOT NULL,	
	ACTIVE					boolean NOT NULL,
    CREATEDBY				bigint NOT NULL,
	CREATEDDATE     		timestamp without time zone NOT NULL,
	LASTMODIFIEDDATE		timestamp without time zone NOT NULL,
	LASTMODIFIEDBY			bigint NOT NULL,
	VERSION					numeric DEFAULT 0 NOT NULL,  
    CONSTRAINT pk_chairperson PRIMARY KEY (id),
    CONSTRAINT fk_chairperson_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
    CONSTRAINT fk_chairperson_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

CREATE SEQUENCE SEQ_EG_CHAIRPERSON;

--rollback DROP SEQUENCE SEQ_EG_CHAIRPERSON;
--rollback DROP TABLE EG_CHAIRPERSON;

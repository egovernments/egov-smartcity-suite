CREATE TABLE EGPT_BULKBILLGENERATION
(
 id BIGINT NOT NULL,
 WARDNO character varying(25) not null,
 Installment BIGINT not null,
 createddate TIMESTAMP without time zone not null,
 modifieddate TIMESTAMP without time zone,
 createdby BIGINT not null,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_BULKBILLGENERATION PRIMARY KEY(id),
 CONSTRAINT fk_bulkbillgnrtn_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_bulkbillgnrtn_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_bulkbillgnrtn_installment FOREIGN KEY(Installment) REFERENCES eg_installment_master(id)
);
 

--rollback DROP table EGPT_BULKBILLGENERATION

CREATE SEQUENCE SEQ_EGPT_BULKBILLGENERATION;

--ROLLBACK DROP SEQUENCE  SEQ_EGPT_BULKBILLGENERATION;

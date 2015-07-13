DROP table EGPT_BULKBILLGENERATION;

CREATE TABLE EGPT_BULKBILLGENERATION
(
 id BIGINT NOT NULL,
 zone  BIGINT not null,
 ward BIGINT,
 Installment BIGINT not null,
 createddate TIMESTAMP without time zone not null,
 modifieddate TIMESTAMP without time zone,
 createdby BIGINT not null,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_BULKBILLGENERATION PRIMARY KEY(id),
 CONSTRAINT fk_bulkbillgnrtn_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_bulkbillgnrtn_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_bulkbillgnrtn_installment FOREIGN KEY(Installment) REFERENCES eg_installment_master(id),
 CONSTRAINT fk_bulkbillgnrtn_zone FOREIGN KEY(zone) REFERENCES EG_BOUNDARY(id),
 CONSTRAINT fk_bulkbillgnrtn_ward FOREIGN KEY(ward) REFERENCES EG_BOUNDARY(id)
);

--rollback DROP table EGPT_BULKBILLGENERATION




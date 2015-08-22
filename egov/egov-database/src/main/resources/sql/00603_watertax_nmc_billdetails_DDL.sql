
create table egwtr_nonmetered_billdetails
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  billNo character varying(20),
  installmentid bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_nonmetered_billdetails PRIMARY KEY (id),
  CONSTRAINT fk_eg_installment_master_id FOREIGN KEY (installmentid)
      REFERENCES eg_installment_master (id),
  CONSTRAINT fk_nonmetered_bill_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_nonmetered_bill_connectiondetails FOREIGN KEY (connectiondetailsid)
      REFERENCES egwtr_connectiondetails (id),
  CONSTRAINT fk_nonmetered_bill_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id) 
);
create sequence seq_egwtr_nonmetered_billdetails;

--rollback DROP TABLE egwtr_nonmetered_billdetails
--rollback DROP SEQUENCE seq_egwtr_nonmetered_billdetails;

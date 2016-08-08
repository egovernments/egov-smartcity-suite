
CREATE TABLE eglc_counter_affidavit 
(	
   id  bigint NOT NULL, 
   legalcase bigint NOT NULL, 
   counterAffidavitduedate date, 
   counterAffidavitapprovaldate date,
   CONSTRAINT pk_eglc_counter_affidavit PRIMARY KEY (id),
   CONSTRAINT fk_counter_affidavit_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
);
CREATE SEQUENCE seq_eglc_counter_affidavit;
CREATE INDEX idx_counter_affidavit_legalcase ON eglc_counter_affidavit (legalcase);
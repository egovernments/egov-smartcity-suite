CREATE TABLE egcl_approver_remitter 
( id BIGINT NOT NULL, 
  approver BIGINT NOT NULL REFERENCES eg_user(id), 
  remitter BIGINT NOT NULL REFERENCES eg_user(id), 
  isactive BOOLEAN NOT NULL DEFAULT TRUE, 
  createdby BIGINT, 
  createddate TIMESTAMP WITHOUT TIME ZONE, 
  lastmodifiedby BIGINT, 
  lastmodifieddate TIMESTAMP WITHOUT TIME ZONE, 
  version BIGINT, 
  CONSTRAINT pk_egcl_approver_remitter PRIMARY KEY(id), 
  CONSTRAINT key_egcl_approver_remitter UNIQUE(approver, remitter));
  
CREATE SEQUENCE seq_egcl_approver_remitter START WITH 1 INCREMENT BY 1;
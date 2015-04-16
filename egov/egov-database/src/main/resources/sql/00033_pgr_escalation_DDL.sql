CREATE TABLE pgr_escalation (
	id BIGINT NOT NULL,
	complaint_type_id BIGINT,
	designation_id BIGINT,
	lastmodifiedby BIGINT,
	lastmodifieddate TIMESTAMP WITHOUT TIME ZONE,
	createdby BIGINT,
	createddate TIMESTAMP WITHOUT TIME ZONE 
);

ALTER TABLE pgr_escalation ADD CONSTRAINT PK_ESCALATION_ID PRIMARY KEY (id);

ALTER TABLE pgr_escalation ADD CONSTRAINT FK_PGR_ESCALATION_DESIG_ID FOREIGN KEY (designation_id) 
    REFERENCES eg_designation (designationid); 

ALTER TABLE pgr_escalation ADD CONSTRAINT FK_PGR_ESCALATION_COM_TYPE_ID FOREIGN KEY (complaint_type_id) 
    REFERENCES pgr_complainttype (id);

ALTER TABLE pgr_escalation ADD CONSTRAINT FK_PGR_ESCALATION_LASTMODIFIEDBY FOREIGN KEY (lastmodifiedby)
    REFERENCES eg_user (id_user);
  
ALTER TABLE pgr_escalation ADD CONSTRAINT FK_PGR_ESCALATION_CREATEDBY FOREIGN KEY (createdby)
    REFERENCES eg_user (id_user);

CREATE SEQUENCE SEQ_PGR_ESCALATION;

--rollback DROP TABLE pgr_escalation;

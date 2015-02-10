CREATE SEQUENCE seq_pgr_complainant;

CREATE TABLE pgr_complainant
(
  id bigint NOT NULL,
  email character varying(100),
  mobile character varying(20),
  name character varying(150),
  userdetail bigint,
  CONSTRAINT pk_complainant PRIMARY KEY (id),
  CONSTRAINT fk_complainant_user FOREIGN KEY (userdetail)
      REFERENCES eg_user (id_user) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE seq_pgr_receiving_center;

CREATE TABLE pgr_receiving_center (
 id bigint NOT NULL,
 name character varying(100),
 CONSTRAINT pk_receiving_center PRIMARY KEY (id)
);

CREATE SEQUENCE seq_pgr_complaint;

CREATE TABLE pgr_complaint
(
  id bigint NOT NULL,
  crn character varying(100),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  complainttype bigint NOT NULL,
  complainant bigint NOT NULL,
  assignee bigint,
  location bigint,
  status character varying(50) NOT NULL,
  details character varying(500) NOT NULL,
  landmarkdetails character varying(200),
  receivingmode smallint,
  receivingcenter bigint,
  lat double precision,
  lng double precision,
  CONSTRAINT pk_complaint PRIMARY KEY (id),
  CONSTRAINT fk_complaint_ FOREIGN KEY (complainant)
      REFERENCES pgr_complainant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_complaint_complainttype FOREIGN KEY (complainttype)
      REFERENCES pgr_complainttype (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_complaint_boundary FOREIGN KEY (location)
      REFERENCES eg_boundary (id_bndry) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comp_receiving_center FOREIGN KEY (receivingcenter) 
      REFERENCES pgr_receiving_center (id) MATCH FULL,
  CONSTRAINT fk_complaint_position FOREIGN KEY (assignee)
      REFERENCES eg_position (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_complaint_crn UNIQUE (crn)
);


CREATE INDEX idx_pgr_complaint_boundary ON pgr_complaint(location);
CREATE INDEX idx_pgr_complaint_complainant ON pgr_complaint(complainant);
CREATE INDEX idx_pgr_complaint_complainttype ON pgr_complaint(complainttype);
CREATE INDEX idx_pgr_complaint_user ON pgr_complaint(assignee);
CREATE INDEX idx_pgr_complainttype_department ON pgr_complainttype(dept_id);
CREATE INDEX idx_pgr_comp_receiving_center ON pgr_complaint(receivingcenter);
CREATE INDEX idx_pgr_complainant_user ON pgr_complainant(userdetail);

--rollback DROP TABLE pgr_complaint;
--rollback DROP SEQUENCE seq_pgr_complaint;
--rollback DROP TABLE pgr_receiving_center;
--rollback DROP SEQUENCE seq_pgr_receiving_center;
--rollback DROP TABLE pgr_complainant;
--rollback DROP SEQUENCE seq_pgr_complainant;

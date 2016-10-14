------------------START------------------
CREATE TABLE egw_contractor_mb_header
(
  id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  mb_refno varchar(1024) NOT NULL,
  mb_date timestamp without time zone NOT NULL,
  status_id bigint,
  document_number bigint,   
  mb_amount double precision NOT NULL,
  remarks varchar(1024) NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  version bigint,
  CONSTRAINT pk_contractor_mb_header PRIMARY KEY (id),
  CONSTRAINT fk_contractor_mbh_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_mbh_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_mbh_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_mbh_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_contractor_mbh_woe ON egw_contractor_mb_header USING btree (workorder_estimate_id);
CREATE INDEX idx_contractor_mbh_status ON egw_contractor_mb_header USING btree (status_id);

CREATE SEQUENCE seq_egw_contractor_mb_header;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractor_mb_details
(
  id bigint NOT NULL,
  contractor_mbheader_id bigint NOT NULL,
  wo_activity_id bigint NOT NULL,
  quantity double precision NOT NULL,
  rate double precision NOT NULL,
  amount double precision NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,  
  version bigint,
  CONSTRAINT pk_contractor_mb_details PRIMARY KEY (id),
  CONSTRAINT fk_contractor_mbd_mbh FOREIGN KEY (contractor_mbheader_id)
      REFERENCES egw_contractor_mb_header (id), 
  CONSTRAINT fk_mbd_woa FOREIGN KEY (wo_activity_id)
      REFERENCES egw_workorder_activity (id),
  CONSTRAINT fk_mbd_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_mbd_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_contractor_mbd_woa ON egw_contractor_mb_details USING btree (wo_activity_id);
CREATE INDEX idx_contractor_mbd_mbheader ON egw_contractor_mb_details USING btree (contractor_mbheader_id);

CREATE SEQUENCE seq_egw_contractor_mb_details;
-------------------END-------------------
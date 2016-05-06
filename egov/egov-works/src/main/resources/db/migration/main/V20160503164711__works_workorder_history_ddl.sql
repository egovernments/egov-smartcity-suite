CREATE TABLE EGW_WORKORDER_HISTORY
(
  id bigint NOT NULL,
  workorder bigint NOT NULL,
  workorderamount double precision NOT NULL,
  revisedworkorderamount double precision NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,
  CONSTRAINT pk_workorder_history PRIMARY KEY (id),
  CONSTRAINT fk_workorder_history_workorder FOREIGN KEY (workorder) REFERENCES egw_workorder (id),
  CONSTRAINT fk_workorder_history_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
  CONSTRAINT fk_workorder_history_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id)
);

CREATE INDEX idx_workorder_history_workorder ON EGW_WORKORDER_HISTORY USING btree (workorder);

CREATE SEQUENCE SEQ_EGW_WORKORDER_HISTORY;

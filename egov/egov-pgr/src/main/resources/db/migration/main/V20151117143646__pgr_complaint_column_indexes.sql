CREATE INDEX idx_pgr_complaint_department
  ON egpgr_complaint
  USING btree
  (department);

CREATE INDEX idx_pgr_complaint_childlocation
  ON egpgr_complaint
  USING btree
  (childlocation);

CREATE INDEX idx_pgr_complaint_receivingcenter
  ON egpgr_complaint
  USING btree
  (receivingcenter);

CREATE INDEX idx_pgr_complaint_status
  ON egpgr_complaint
  USING btree
  (status);

CREATE INDEX idx_pgr_router_complainttypeid
  ON egpgr_router
  USING btree
  (complainttypeid);

CREATE INDEX idx_pgr_router_bndryid
  ON egpgr_router
  USING btree
  (bndryid);

CREATE INDEX idx_pgr_router_position
  ON egpgr_router
  USING btree
  ("position");

CREATE INDEX idx_pgr_escalation_complaint_type_id
  ON egpgr_escalation
  USING btree
  (complaint_type_id);

CREATE INDEX idx_pgr_escalation_designation_id
  ON egpgr_escalation
  USING btree
  (designation_id);




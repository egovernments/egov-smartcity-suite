ALTER TABLE egw_lineestimate ADD COLUMN contractCommitteeApprovalNumber character varying(100);
ALTER TABLE egw_lineestimate ADD COLUMN contractCommitteeApprovalDate timestamp without time zone;
ALTER TABLE egw_lineestimate ADD COLUMN standingCommitteeApprovalNumber character varying(100);
ALTER TABLE egw_lineestimate ADD COLUMN standingCommitteeApprovalDate timestamp without time zone;
ALTER TABLE egw_lineestimate ADD COLUMN governmentApprovalNumber character varying(100);
ALTER TABLE egw_lineestimate ADD COLUMN governmentApprovalDate timestamp without time zone;
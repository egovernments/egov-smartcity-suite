DROP MATERIALIZED VIEW IF EXISTS egpt_view_propertyinfo;

ALTER TABLE egpt_mv_propertyinfo ALTER COLUMN regd_doc_no TYPE character varying(100);

CREATE MATERIALIZED VIEW egpt_view_propertyinfo AS
  SELECT * FROM egpt_mv_propertyinfo;

CREATE INDEX idx_egptpmv_upicno ON egpt_view_propertyinfo(upicno);
CREATE INDEX idx_egptpmv_basicpropertyid ON egpt_view_propertyinfo(basicpropertyid);
CREATE UNIQUE INDEX upicno_un_idx ON egpt_view_propertyinfo(upicno);

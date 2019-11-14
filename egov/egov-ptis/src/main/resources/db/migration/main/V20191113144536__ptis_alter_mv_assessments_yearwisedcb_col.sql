ALTER TABLE egpt_mv_assessments ADD COLUMN yearwisedcb boolean DEFAULT false;
COMMENT ON COLUMN egpt_mv_assessments.yearwisedcb IS 'This flag will be used to update yearwise dcb on daily basis';
UPDATE egpt_mv_assessments SET yearwisedcb = true;

























DO $$ 
BEGIN
BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN electionwardid bigint;
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column electionwardid already exists in egpt_mv_propertyinfo.';
END;

BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN usage character varying(256);
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column usage already exists in egpt_mv_propertyinfo.';
END;

BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN longitude double precision;
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column longitude already exists in egpt_mv_propertyinfo.';
END;

BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN latitude double precision;
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column latitude already exists in egpt_mv_propertyinfo.';
END;

BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN category_type character varying(256);
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column category_type already exists in egpt_mv_propertyinfo.';
END;

BEGIN
CREATE TABLE egpt_mv_assessments
(
	id bigint NOT NULL,
	assessmentno character varying(64) NOT NULL,
	date_modified timestamp without time zone NOT NULL, -- Last Trasnaction date of on assessment
	type character varying(64),
	isUpdated boolean DEFAULT false
);
EXCEPTION
WHEN duplicate_table THEN RAISE NOTICE 'table egpt_mv_assessments already exists';
END;

BEGIN
create sequence seq_egpt_mv_assessments;
EXCEPTION
WHEN duplicate_table THEN RAISE NOTICE 'sequence seq_egpt_mv_assessments already';
END;

ALTER TABLE egpt_mv_propertyinfo ADD COLUMN regd_doc_no character varying(64);
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN regd_doc_date date;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN pattano character varying(256);
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN marketvalue double precision;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN capitalvalue double precision;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN assessmentdate date;

END;
$$


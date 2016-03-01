ALTER TABLE tds ALTER isactive TYPE bool USING CASE WHEN isactive='0' THEN FALSE ELSE TRUE END;

ALTER TABLE tds ADD column version bigint;

ALTER TABLE eg_billregister ADD COLUMN version bigint;

ALTER TABLE eg_billregister ALTER COLUMN isactive TYPE boolean USING CASE WHEN isactive = 0 THEN FALSE WHEN isactive = 1 THEN TRUE ELSE NULL END;

ALTER TABLE eg_billregister ALTER COLUMN worksdetailid TYPE VARCHAR(50);

ALTER TABLE eg_billregistermis ALTER COLUMN budgetcheckreq TYPE boolean USING CASE WHEN budgetcheckreq = 0 THEN FALSE WHEN budgetcheckreq = 1 THEN TRUE ELSE NULL END;

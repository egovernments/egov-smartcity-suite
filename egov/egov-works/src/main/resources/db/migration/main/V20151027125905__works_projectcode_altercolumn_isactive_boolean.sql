
ALTER TABLE egw_projectcode ALTER COLUMN isactive DROP DEFAULT;
ALTER TABLE egw_projectcode ALTER COLUMN isactive TYPE boolean USING CASE isactive WHEN '1' THEN true ELSE '0' END;
ALTER TABLE egw_projectcode ALTER COLUMN isactive SET DEFAULT false;


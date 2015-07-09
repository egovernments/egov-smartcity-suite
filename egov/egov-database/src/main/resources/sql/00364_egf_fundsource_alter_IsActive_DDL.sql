  
ALTER TABLE fundsource ALTER COLUMN isactive TYPE boolean USING CASE WHEN isactive = 0 THEN FALSE WHEN isactive = 1 THEN TRUE ELSE NULL END;
    

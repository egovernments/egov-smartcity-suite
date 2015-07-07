ALTER TABLE chartofaccounts ALTER COLUMN isactiveforposting TYPE boolean USING CASE WHEN isactiveforposting = 0 THEN FALSE WHEN isactiveforposting = 1 THEN TRUE ELSE NULL END;
    
ALTER TABLE chartofaccounts ALTER COLUMN functionreqd TYPE boolean USING CASE WHEN functionreqd = 0 THEN FALSE WHEN functionreqd = 1 THEN TRUE ELSE NULL END;
    
ALTER TABLE chartofaccounts ALTER COLUMN budgetcheckreq TYPE boolean USING CASE WHEN budgetcheckreq = 0 THEN FALSE WHEN budgetcheckreq = 1 THEN TRUE ELSE NULL END;
    
    

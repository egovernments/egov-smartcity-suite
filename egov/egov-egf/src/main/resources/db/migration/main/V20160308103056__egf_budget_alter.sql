ALTER TABLE egf_budget ALTER isactivebudget TYPE bool USING CASE WHEN isactivebudget='0' THEN FALSE ELSE TRUE END;

ALTER TABLE egf_budget ALTER isprimarybudget TYPE bool USING CASE WHEN isprimarybudget='0' THEN FALSE ELSE TRUE END;

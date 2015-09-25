ALTER TABLE egpt_property_usage_master DROP COLUMN resd_or_nonresd;
ALTER TABLE egpt_property_usage_master ADD COLUMN ISRESIDENTIAL boolean;
UPDATE egpt_property_usage_master set ISRESIDENTIAL = false;
UPDATE egpt_property_usage_master set ISRESIDENTIAL = true WHERE usg_name_local='Residential';
ALTER TABLE egpt_property_usage_master ALTER COLUMN ISRESIDENTIAL SET NOT NULL;
ALTER TABLE egpt_property_usage_master ALTER COLUMN usage_factor DROP NOT NULL;

--ALTER TABLE egpt_property_usage_master ALTER COLUMN usage_factor SET NOT NULL;
--ALTER TABLE egpt_property_usage_master ALTER COLUMN ISRESIDENTIAL DROP NOT NULL;
--UPDATE egpt_property_usage_master set ISRESIDENTIAL = false;
--ALTER TABLE egpt_property_usage_master DROP COLUMN ISRESIDENTIAL;
--ALTER TABLE egpt_property_usage_master ADD COLUMN resd_or_nonresd character varying(50);
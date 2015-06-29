ALTER TABLE egpt_property_owner RENAME TO egpt_property_owner_info;
ALTER TABLE egpt_property_owner_info RENAME ownerid to id;
ALTER TABLE egpt_property_owner_info ADD COLUMN owner bigint;
CREATE SEQUENCE seq_egpt_property_owner_info;
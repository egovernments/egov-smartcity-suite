ALTER TABLE egpt_property_status_values ALTER COLUMN building_permission_no type varchar(25);
UPDATE egpt_basic_property SET applicationno='01052015-XY-00001';
ALTER TABLE egpt_basic_property ALTER COLUMN applicationno SET NOT NULL;

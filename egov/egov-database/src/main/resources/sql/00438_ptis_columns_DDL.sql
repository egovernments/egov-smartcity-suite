ALTER TABLE egpt_property_status_values DROP COLUMN building_permission_no;
ALTER TABLE egpt_property_status_values DROP COLUMN building_permission_date;
ALTER TABLE egpt_property_detail ADD COLUMN building_permission_no character varying(30);
ALTER TABLE egpt_property_detail ADD COLUMN building_permission_date TIMESTAMP without time zone;
ALTER TABLE egpt_property_detail ADD COLUMN deviation_percentage character varying(30);


--rollback ALTER TABLE egpt_property_status_values ADD COLUMN building_permission_no BIGINT;
--rollback ALTER TABLE egpt_property_status_values ADD COLUMN building_permission_date TIMESTAMP without time zone;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN building_permission_no;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN building_permission_date;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN deviation_percentage;

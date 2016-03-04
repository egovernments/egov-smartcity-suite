ALTER TABLE egpt_property_detail DROP COLUMN building_permission_no;
ALTER TABLE egpt_property_detail DROP COLUMN building_permission_date;
ALTER TABLE egpt_property_detail DROP COLUMN deviation_percentage;
ALTER TABLE egpt_property_detail DROP COLUMN buildingplandetails_checked;

ALTER TABLE egpt_floor_detail ADD COLUMN building_permission_no character varying(30);
ALTER TABLE egpt_floor_detail ADD COLUMN building_permission_date timestamp without time zone;
ALTER TABLE egpt_floor_detail ADD COLUMN building_permission_pintharea double precision;
ALTER TABLE egpt_floor_detail DROP COLUMN drainage;
ALTER TABLE egpt_floor_detail DROP COLUMN no_of_seats;
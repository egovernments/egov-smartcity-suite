ALTER TABLE egwtr_meter_connection_details ADD COLUMN ismeterdamaged boolean;

--rollback ALTER TABLE egwtr_meter_connection_details drop column ismeterdamaged;

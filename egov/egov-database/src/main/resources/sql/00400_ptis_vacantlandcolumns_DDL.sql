ALTER TABLE egpt_property_detail ADD COLUMN current_capitalValue DOUBLE PRECISION;
ALTER TABLE egpt_property_detail ADD COLUMN marketvalue DOUBLE PRECISION;
ALTER TABLE egpt_property_detail ADD COLUMN pattanumber character varying(32);

--rollback ALTER TABLE egpt_property_detail DROP COLUMN current_capitalValue;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN marketvalue;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN pattanumber;


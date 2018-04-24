DROP INDEX IF EXISTS idx_id_property;
DROP INDEX IF EXISTS idx_bp_propertyid;
DROP INDEX IF EXISTS idx_basic_property;
DROP INDEX IF EXISTS idx_basicproperty;

CREATE INDEX idx_id_property on egpt_ptdemand(id_property);
CREATE INDEX idx_bp_propertyid on egpt_basic_property(propertyid);
CREATE INDEX idx_basic_property on egpt_property_status_values(id_basic_property); 
CREATE INDEX idx_basicproperty on egpt_property_owner_info(basicproperty);    


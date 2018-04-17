CREATE INDEX IF NOT EXISTS idx_id_property on egpt_ptdemand(id_property);
CREATE INDEX IF NOT EXISTS idx_bp_propertyid on egpt_basic_property(propertyid);  
CREATE INDEX IF NOT EXISTS idx_basic_property on egpt_property_status_values(id_basic_property);    
CREATE INDEX IF NOT EXISTS idx_basicproperty on egpt_property_owner_info(basicproperty);    


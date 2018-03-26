DROP INDEX bp_isactive_idx ;
DROP INDEX bp_idpropertyid_idx;
DROP INDEX pid_ward_adm_idx;


CREATE INDEX idx_bp_isactive ON egpt_basic_property (isactive);
CREATE INDEX idx_bp_idpropertyid ON egpt_basic_property (id_propertyid);
CREATE INDEX idx_pid_ward_adm_idx ON egpt_propertyid (ward_adm_id);




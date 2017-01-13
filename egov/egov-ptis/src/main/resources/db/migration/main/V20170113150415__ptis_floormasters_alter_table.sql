
--------------- Property Usage Master ----------------
ALTER TABLE egpt_property_usage_master ADD column VERSION numeric DEFAULT 0;
ALTER TABLE egpt_property_usage_master RENAME CREATED_DATE TO createdDate;
ALTER TABLE egpt_property_usage_master RENAME MODIFIED_DATE TO lastModifiedDate;
ALTER TABLE egpt_property_usage_master RENAME CREATED_BY TO createdBy;
ALTER TABLE egpt_property_usage_master RENAME MODIFIED_BY TO lastModifiedBy;


---------------- Property Occupancy ----------------
ALTER TABLE egpt_occupation_type_master ADD column VERSION numeric DEFAULT 0;
ALTER TABLE egpt_occupation_type_master RENAME CREATED_DATE TO createdDate;
ALTER TABLE egpt_occupation_type_master RENAME MODIFIED_DATE TO lastModifiedDate;
ALTER TABLE egpt_occupation_type_master RENAME CREATED_BY TO createdBy;
ALTER TABLE egpt_occupation_type_master RENAME MODIFIED_BY TO lastModifiedBy;


---------------- Structure Classification ----------------
ALTER TABLE egpt_struc_cl ADD column VERSION numeric DEFAULT 0;
ALTER TABLE egpt_struc_cl RENAME CREATED_DATE TO createdDate;
ALTER TABLE egpt_struc_cl RENAME MODIFIED_DATE TO lastModifiedDate;
ALTER TABLE egpt_struc_cl RENAME CREATED_BY TO createdBy;
ALTER TABLE egpt_struc_cl RENAME MODIFIED_BY TO lastModifiedBy;


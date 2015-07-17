alter table egpt_property_detail add buildingplandetails_checked boolean;
alter table egpt_property_detail add appurtenantland_checked boolean;  

--rollback alter table egpt_property_detail drop appurtenantland_checked;
--rollback alter table egpt_property_detail drop buildingplandetails_checked;
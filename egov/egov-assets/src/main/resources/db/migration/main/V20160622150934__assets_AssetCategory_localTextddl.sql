ALTER TABLE egasset_asset ADD COLUMN locationDetails_Id bigint;

ALTER TABLE egasset_category_propertytype ADD COLUMN localText varchar(200);
	
ALTER TABLE egasset_asset ADD CONSTRAINT fk_asset_locationdetails FOREIGN KEY (locationDetails_Id)
	REFERENCES egasset_locationdetails (id);	
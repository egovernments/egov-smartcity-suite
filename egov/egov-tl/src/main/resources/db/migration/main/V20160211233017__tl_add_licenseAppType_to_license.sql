ALTER TABLE egtl_license add column licenseAppType bigint ;
ALTER TABLE ONLY egtl_license ADD CONSTRAINT fk_egtl_licenseAppType FOREIGN KEY (licenseAppType) REFERENCES egtl_mstr_app_type(id);
UPDATE egtl_license set licenseAppType= (select id from egtl_mstr_app_type where name='New');
ALTER TABLE egtl_license ALTER COLUMN licenseAppType SET NOT NULL;
																																																																																																																																																																																																																					

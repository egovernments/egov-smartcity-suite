--Sequence to generate License Number
CREATE SEQUENCE egtl_license_number;

--Add legacy column for already existing licenses
ALTER TABLE egtl_license ADD COLUMN islegacy boolean DEFAULT false;




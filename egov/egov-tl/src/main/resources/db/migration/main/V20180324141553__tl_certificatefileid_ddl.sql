
ALTER TABLE EGTL_LICENSE ADD COLUMN certificateFileId varchar(40);

update egtl_license  set certificatefileid = digisignedcertfilestoreid;
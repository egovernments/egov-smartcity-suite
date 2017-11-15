alter table egtl_demandgenerationlogdetail  add column licenseNumber varchar(50);
alter table egtl_demandgenerationlogdetail  add column licenseId bigint;
update egtl_demandgenerationlogdetail set licenseid=license, licensenumber=(select license_number from egtl_license where id=license);
alter table egtl_demandgenerationlogdetail  drop column license;
alter table egtl_demandgenerationlogdetail ADD CONSTRAINT unq_egtl_demandgenlog_license UNIQUE(demandgenerationlog, licenseId);
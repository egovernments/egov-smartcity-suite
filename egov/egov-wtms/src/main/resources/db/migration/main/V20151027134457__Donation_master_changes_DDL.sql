drop table egwtr_donation_master;
drop sequence seq_egwtr_donation_master;

delete from egwtr_donation_details;
delete from egwtr_donation_header;

ALTER TABLE egwtr_donation_header DROP COLUMN minsumpcapacity;
ALTER TABLE egwtr_donation_header DROP COLUMN maxsumpcapacity;
ALTER TABLE egwtr_donation_header ADD COLUMN propertytype bigint NOT NULL;

update eg_action set url = '/masters/donationMaster.action' where name = 'DonationMasterDetailsScreen';
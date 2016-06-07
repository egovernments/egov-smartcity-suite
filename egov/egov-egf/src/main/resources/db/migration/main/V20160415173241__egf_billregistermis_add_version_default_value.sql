ALTER TABLE eg_billregistermis DROP COLUMN version RESTRICT;

ALTER TABLE eg_billdetails DROP COLUMN version RESTRICT;

ALTER TABLE eg_billpayeedetails DROP COLUMN version RESTRICT;

ALTER TABLE eg_bill_subtype DROP COLUMN version RESTRICT;


ALTER TABLE eg_billregistermis ADD COLUMN version numeric DEFAULT 0;

ALTER TABLE eg_billdetails ADD COLUMN version numeric DEFAULT 0;

ALTER TABLE eg_billpayeedetails ADD COLUMN version numeric DEFAULT 0;

ALTER TABLE eg_bill_subtype ADD COLUMN version numeric  DEFAULT 0;

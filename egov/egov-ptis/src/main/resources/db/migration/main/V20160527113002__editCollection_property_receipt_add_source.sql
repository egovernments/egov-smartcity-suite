alter table egpt_property_receipts add column source character varying(1) default 'M';
alter table egpt_property_receipts add column remarks character varying(256);

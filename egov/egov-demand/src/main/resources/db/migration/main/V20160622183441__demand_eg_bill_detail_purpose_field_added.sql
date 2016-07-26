alter table eg_bill_details add column purpose character varying(50);
update eg_bill_details set purpose='OTHERS' where purpose is null;
alter table eg_bill_details alter column purpose set not null;
--alter table eg_bill_details drop column if exists purpose;

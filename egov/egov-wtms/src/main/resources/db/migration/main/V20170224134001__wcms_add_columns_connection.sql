ALTER TABLE egwtr_connectiondetails ADD COLUMN referencenumber character varying(50);
ALTER TABLE egwtr_connection ADD COLUMN duplicateconsumernumber character varying(50) ;
ALTER TABLE egwtr_connectiondetails ADD COLUMN deactivatereason character varying(1024);
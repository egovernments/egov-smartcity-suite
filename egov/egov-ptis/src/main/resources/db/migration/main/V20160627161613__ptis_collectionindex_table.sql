DO $$ 
BEGIN
BEGIN
CREATE TABLE egpt_collectionindex (
receiptnumber character varying(25),
receiptdate timestamp without time zone,
createdDate timestamp without time zone,
modifieddate timestamp without time zone,
billingservice character varying(25),
paymentmode character varying(25),
arrearamount double precision,
arrearLibCess double precision,
currentamount double precision,
currentlibces double precision,
penalty double precision,
latepaymentcharges double precision,
totalamount double precision,
advanceamount double precision,
rebateamount double precision,
channel character varying(25),
paymentgateway character varying(25),
billnumber character varying(25),
consumercode character varying(25),
frominstallment character varying(25),
toinstallment character varying(25),
status character varying(25),
payeename character varying(150),
ulbname character varying(25),
districtname character varying(25),
regionname character varying(25),
createdBy bigint,
modifiedBy bigint
);
EXCEPTION
WHEN duplicate_table THEN RAISE NOTICE 'table egpt_collectionindex already exists';
END;

END;
$$

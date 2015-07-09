INSERT INTO egpt_apartment(id,version,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby) values
(nextval('SEQ_EGPT_APARTMENTS'),0,'Spring Fields Appartment','Spring',current_timestamp,current_timestamp,1,1);
INSERT INTO egpt_apartment(id,version,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby) values
(nextval('SEQ_EGPT_APARTMENTS'),0,'Mantri Square','MS',current_timestamp,current_timestamp,1,1);
INSERT INTO egpt_apartment(id,version,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby) values
(nextval('SEQ_EGPT_APARTMENTS'),0,'Alankara Plaza','A-plaza',current_timestamp,current_timestamp,1,1);
INSERT INTO egpt_apartment(id,version,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby) values
(nextval('SEQ_EGPT_APARTMENTS'),0,'Nitesh Estates','Nitesh',current_timestamp,current_timestamp,1,1);

--rollback DELETE FROM egpt_apartment;

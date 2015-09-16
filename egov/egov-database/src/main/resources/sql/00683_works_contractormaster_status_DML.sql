
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Active',now(),'Active',1);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Inactive',now(),'Inactive',2);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Black-listed',now(),'Black-listed',3);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Debarred',now(),'Debarred',4);

--rollback delete from EGW_STATUS where moduletype='Contractor';

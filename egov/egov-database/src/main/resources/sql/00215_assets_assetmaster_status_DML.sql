
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Created',now(),'Created',1);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','CWIP',now(),'CWIP',2);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Capitalized',now(),'Capitalized',3);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Revaluated',now(),'Revaluated',4);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Disposed',now(),'Disposed',5);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Retired',now(),'Retired',6);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Sold',now(),'Sold',7);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'ASSET','Cancelled',now(),'CANCELLED',8);

--rollback delete from EGW_STATUS where moduletype='ASSET';

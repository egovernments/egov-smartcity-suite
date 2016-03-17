
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Approved',now(),'APPROVED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Cancelled',now(),'CANCELLED',5);

--rollback delete from EGW_STATUS where MODULETYPE='WorkOrder' and code='APPROVED';
--rollback delete from EGW_STATUS where MODULETYPE='WorkOrder' and code='CANCELLED';
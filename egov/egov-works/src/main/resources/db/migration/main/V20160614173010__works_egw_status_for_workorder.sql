Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Created',now(),'CREATED',1);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Rejected',now(),'REJECTED',2);

--rollback delete from egw_status where moduletype='WorkOrder' and code in ('CREATED','REJECTED');

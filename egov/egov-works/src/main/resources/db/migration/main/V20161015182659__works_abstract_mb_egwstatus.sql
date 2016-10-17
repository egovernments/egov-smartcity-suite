Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'MBHeader','Checked',now(),'CHECKED',2);

--rollback delete from EGW_STATUS where MODULETYPE='MBHeader' and code='Checked';

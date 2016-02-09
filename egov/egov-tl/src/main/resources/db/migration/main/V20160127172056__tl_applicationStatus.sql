
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Created',now(),'CREATED',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Inspection Done',now(),'INSPECTIONDONE',1);

 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Approved',now(),'APPROVED',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Collection Amount Paid',now(),'COLLECTIONAMOUNTPAID',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Digital Signature Updated',now(),'DIGITALSIGNUPDATED',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'TRADELICENSE','Certificate Generated',now(),'CERTIFICATEGENERATED',1);
 
	

alter table egtl_license add column egwStatusId bigint;

ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_adtax_status_app FOREIGN KEY (egwStatusId) REFERENCES egw_status(id);

update egtl_license set egwStatusId= (select id from egw_status where moduletype='TRADELICENSE' and code='CERTIFICATEGENERATED') where egwStatusId is null;

ALTER TABLE egtl_license ALTER COLUMN egwStatusId SET NOT NULL;
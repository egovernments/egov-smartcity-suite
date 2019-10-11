----------writeoff status 
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'WRITEOFF',CURRENT_TIMESTAMP,'Y','WRITEOFF');


---------write off document type
Insert into EGPT_DOCUMENT_TYPE (id,name,mandatory,version,transactiontype,id_application_type) values (nextval('seq_egpt_document_type'),'Any other Attachment','false',null,'WRITEOFF', (select id from egpt_application_type where code='WRITEOFF'));

-----------Application type
insert into EGPT_APPLICATION_TYPE (id,code,name,resolutiontime,description,createddate,lastmodifieddate,createdby,lastmodifiedby,version)
 values (nextval('seq_egpt_application_type'),'WRITEOFF','WRITE OFF',360,'WRITE OFF',now(),null,1,null,null);

------write off reason
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'DOUBLE NUMBER','Double Number','Full WriteOff','DOUBLE',1);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'NOT TRACED','Not Traced','Full WriteOff','NOTTRACED',2);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'DEMOLISHED','Demolished','Full WriteOff','DEMOLISHED',3);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'AMALGAMATED','Amalgamated','Full WriteOff','AMALG',4);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'IMPROPERLY ASSESSED','Improperly Assessed ','Partial WriteOff','IMPROPERLY ASSESSED',1);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'WRONG USAGE','Wrong Usage','Partial WriteOff','WRONG USAGE',2);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'WRONG OCCUPATION','Wrong Occupation','Partial WriteOff','WRONG OCCUPATION',3);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'WRONG AGE OF THE BUILDING','Wrong Age of the Building','Partial WriteOff','WRONG AGE BUILDING',4);
Insert into egpt_writeoff_reason (id,version,name,description,type,code,order_id) values (nextval('SEQ_WRITE_OFF_REASON'),1,'WRONG CLASSIFICATION','Wrong Classification','Partial WriteOff','WRONG CLASSIFI',5);

---muation master

Insert into egpt_mutation_master (ID,MUTATION_NAME,MUTATION_DESC,TYPE,CODE,ORDER_ID) values (nextval('SEQ_EGPT_MUTATION_MASTER'),'FULL WRITEOFF','Full WriteOff','WRITEOFF','FULL WRITEOFF',1);

Insert into egpt_mutation_master (ID,MUTATION_NAME,MUTATION_DESC,TYPE,CODE,ORDER_ID) values (nextval('SEQ_EGPT_MUTATION_MASTER'),'PARTIAL WRITEOFF','Partial WriteOff','WRITEOFF','PARTIAL WRITEOFF',2);

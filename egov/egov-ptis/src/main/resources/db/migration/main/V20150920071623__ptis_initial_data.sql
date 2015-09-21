-----------------START-------------------
Insert into EGPT_SRC_OF_INFO (ID,SOURCE_NAME,LASTUPDATEDTIMESTAMP,SOURCE_NAME_LOCAL,SRC_SHT_NAME) values (nextval('SEQ_EGPT_SRC_OF_INFO'),'Municipal Records',CURRENT_TIMESTAMP,null,'MNCPL-RECORDS');
------------------END---------------------

-----------------START-------------------
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'OBJECTED',CURRENT_TIMESTAMP,'Y','OBJECTED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'NOTICE155ISSUED',CURRENT_TIMESTAMP,'Y','NOTICE155ISSUED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'WARRANT APPLICATION PREPARED',CURRENT_TIMESTAMP,'Y','WARRANT APPLICATION PREPARED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Warrant application approved',CURRENT_TIMESTAMP,'Y','WARRANT APPLICATION APPROVED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'WARRANT NOTICE CREATED',CURRENT_TIMESTAMP,'Y','WARRANT NOTICE CREATED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'WARRANT NOTICE ISSUED',CURRENT_TIMESTAMP,'Y','WARRANT NOTICE ISSUED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'CEASE NOTICE CREATED',CURRENT_TIMESTAMP,'Y','CEASE NOTICE CREATED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'CEASE NOTICE ISSUED',CURRENT_TIMESTAMP,'Y','CEASE NOTICE ISSUED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Inactive',CURRENT_TIMESTAMP,'Y','INACTIVE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Self Assessed and Unverified',CURRENT_TIMESTAMP,'Y',null);
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Assessed',CURRENT_TIMESTAMP,'Y','ASSESSED');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Unassessed',CURRENT_TIMESTAMP,'Y',null);
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Create',CURRENT_TIMESTAMP,'Y','CREATE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Change',CURRENT_TIMESTAMP,'Y','CHANGE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Modify',CURRENT_TIMESTAMP,'Y','MODIFY');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Active',CURRENT_TIMESTAMP,'Y','ACTIVE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Marked For Deactivation',CURRENT_TIMESTAMP,'Y','MARK_DEACTIVE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Bifurcation',CURRENT_TIMESTAMP,'Y','BIFURCATE');
Insert into egpt_status (ID,STATUS_NAME,created_date,IS_ACTIVE,CODE) values (nextval('SEQ_EGPT_STATUS'),'Amalgamation',CURRENT_TIMESTAMP,'Y','AMALG');
INSERT INTO egpt_status  (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'WORKFLOW',CURRENT_TIMESTAMP,'Y','WORKFLOW');
INSERT INTO egpt_status  (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'APPROVED',CURRENT_TIMESTAMP,'Y','APPROVED');
------------------END---------------------

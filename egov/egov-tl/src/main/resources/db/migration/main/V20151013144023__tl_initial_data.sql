--Trade License Module
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License','t','tl',null,'Trade License', null);


--Installment master
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-14','DD-MM-YY'),to_date('01-04-14','DD-MM-YY'),to_date('31-03-15','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/14-15','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/15-16','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-16','DD-MM-YY'),to_date('01-04-16','DD-MM-YY'),to_date('31-03-17','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/17-17','Yearly');


--demand reason master
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'License Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'License Fee', 3, current_timestamp, current_timestamp,'t');
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Motor Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'Motor Fee', 4, current_timestamp, current_timestamp,'t');
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Workforce Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'Workforce Fee', 4, current_timestamp, current_timestamp,'t');

--License Type Master
Insert into  EGTL_MSTR_LICENSE_TYPE (ID,NAME,ID_MODULE) values (nextval('SEQ_EGTL_MSTR_LICENSE_TYPE'),'TradeLicense',(select id from eg_module where name='Trade License' and parentmodule is null));


--Business Nature Master
Insert into  EGTL_MSTR_BUSINESS_NATURE (ID,NAME,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('SEQ_EGTL_MSTR_BUSINESS_NATURE'),'Permanent',Current_date,Current_date,1,1);
Insert into  EGTL_MSTR_BUSINESS_NATURE (ID,NAME,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('SEQ_EGTL_MSTR_BUSINESS_NATURE'),'Temporary',Current_date,Current_date,1,1);


--Status Master
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Acknowledged',Current_date,'1','ACK',1);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Active',Current_date,'1','ACT',2);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Rejected',Current_date,'1','REJ',3);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Objected',Current_date,'1','OBJ',4);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Suspended',Current_date,'1','SUS',5);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Cancelled',Current_date,'1','CAN',6);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'Expired',Current_date,'1','EXP',7);
Insert into  EGTL_MSTR_STATUS (ID,STATUS_NAME,LASTUPDATEDTIMESTAMP,IS_ACTIVE,CODE,ORDER_ID) values (nextval('SEQ_EGTL_MSTR_STATUS'),'UnderWorkflow',Current_date,'1','UWF',8);


--AppType Master
insert into EGTL_MSTR_APP_TYPE values (nextval('seq_EGTL_MSTR_APP_TYPE'),'New',now(),now(),1,1,0);
insert into EGTL_MSTR_APP_TYPE values (nextval('seq_EGTL_MSTR_APP_TYPE'),'Renew',now(),now(),1,1,0);


--Fee Type Master
insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'License Fee','LF',0,now(),1,now(),1,0);
insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'Motor Fee','MF',0,now(),1,now(),1,0);
insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'Workforce Fee','WF',0,now(),1,now(),1,0);


--Roles for Trade License
Insert into EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values (nextval('seq_eg_role'),'TLCreator','TradeLicense creator',NOW(),1,1,NOW(),0);
Insert into EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values (nextval('seq_eg_role'),'TLApprover','TradeLicense Approver',NOW(),1,1,NOW(),0);


------ Workflow type
INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version)
values (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Trade License'), 'TradeLicense', '/tl/newtradelicense/newTradeLicense-showForApproval.action?model.id=:ID',
1, now(), 1, now(), 'Y', 'N', 'org.egov.tl.domain.entity.TradeLicense', 'Trade License', 0);


--Licese Sub Type Master
Insert into  EGTL_MSTR_LICENSE_SUB_TYPE (ID,NAME,CODE,ID_LICENSE_TYPE) values (nextval('SEQ_EGTL_MSTR_LICENSE_SUB_TYPE'),'Temp','SCT',(select id from EGTL_MSTR_LICENSE_TYPE where name='TradeLicense'));


--demand reason
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') from eg_installment_master inst where inst.id_module=(select id_module from eg_module where name='Trade License'));


--reason details
Insert into eg_demand_reason_details (ID,ID_DEMAND_REASON,PERCENTAGE,FROM_DATE,TO_DATE,LOW_LIMIT,HIGH_LIMIT,CREATE_DATE,MODIFIED_DATE,FLAT_AMOUNT,IS_FLATAMNT_MAX) values (nextval('seq_eg_demand_reason_details'),(select id from eg_demand_reason where id_demand_reason_master = (select id from EG_DEMAND_REASON_MASTER where REASONMASTER = 'License Fee') and id_installment = (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2015 00:00:00','dd/MM/yyyy HH24:MI:SS'))),0.5,to_date('01/04/2004 00:00:00','dd/MM/yyyy HH24:MI:SS'),to_date('01/04/2015 23:59:59','dd/MM/yyyy HH24:MI:SS'),1,9999999999,current_timestamp, current_timestamp,0,0);


---App Config for fee
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Is Fee For Permanent and Temporary Same','Is Fee For Permanent and Temporary Same',(select id from eg_module where name='Trade License'));


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Is Fee For New and Renew Same','Is Fee For New and Renew Same',(select id from eg_module where name='Trade License'));

---Appconfig Value for Fee 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Is Fee For Permanent and Temporary Same' AND   MODULE =(select id from eg_module where name='Trade License')),current_date,'Y',0);

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Is Fee For New and Renew Same' AND   MODULE =(select id from eg_module where name='Trade License')),current_date,'Y',0);


--UOM master data
insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'HP','HP',false,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Person','Person',false,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'KiloGram','KiloGram',true,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Ton','Ton',true,1,1,now(),now(),0);


insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Meter','Meter',true,1,1,now(),now(),0);

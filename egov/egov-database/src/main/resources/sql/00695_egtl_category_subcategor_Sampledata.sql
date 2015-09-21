Insert into  EGTL_MSTR_LICENSE_SUB_TYPE (ID,NAME,CODE,ID_LICENSE_TYPE) values (nextval('SEQ_EGTL_MSTR_LICENSE_SUB_TYPE'),'Temp','SCT',(select id from EGTL_MSTR_LICENSE_TYPE where name='TradeLicense'));

Insert into  EGTL_MSTR_CATEGORY (ID,NAME,code,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('seq_egtl_mstr_category'),'Shops','Shops',Current_date,Current_date,1,1);
Insert into  EGTL_MSTR_CATEGORY (ID,NAME,code,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('seq_egtl_mstr_category'),'Hotels','Hotels',Current_date,Current_date,1,1);


Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Super Bazar','109',1,1,(select id from EGTL_MSTR_CATEGORY where name='Shops') ,null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Small Super Bazar','110',1,1,(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Mini Super Bazar','111',1,1,(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Whole Sale','112',1,1,(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY
(ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Retail','113',1,1,(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);


Insert into  EGTL_MSTR_SUB_CATEGORY
(ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'5 Star','16',1,1,(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'3 Star','17',1,1,(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Lodge','18',1,1,(select id from EGTL_MSTR_CATEGORY where name='Hotels'), null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Food Joint','19',1,1,(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Fast Food Center','115',1,1,(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);



--demand reason
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 

(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') from eg_installment_master inst where inst.id_module=(select id_module from eg_module where name='Trade License'));


--reason details
Insert into eg_demand_reason_details (ID,ID_DEMAND_REASON,PERCENTAGE,FROM_DATE,TO_DATE,LOW_LIMIT,HIGH_LIMIT,CREATE_DATE,MODIFIED_DATE,FLAT_AMOUNT,IS_FLATAMNT_MAX) values (nextval('seq_eg_demand_reason_details'),(select id from eg_demand_reason where id_demand_reason_master = (select id from EG_DEMAND_REASON_MASTER where REASONMASTER = 'License Fee') and id_installment = (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2015 00:00:00','dd/MM/yyyy HH24:MI:SS'))),0.5,to_date('01/04/2004 00:00:00','dd/MM/yyyy HH24:MI:SS'),to_date('01/04/2015 23:59:59','dd/MM/yyyy HH24:MI:SS'),1,9999999999,current_timestamp, current_timestamp,0,0);






#UP

/****28/11/2008 PensionPayment status added in egw_status table************/

INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'PensionPayment','Created','28/nov/2008');
INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'PensionPayment','Disbursed','28/nov/2008');






/****11/12/2008 pension disbursement menuee tree****************/

Insert into eg_module
     (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
   Values
     (SEQ_MODULEMASTER.nextval, 'PensionDisbursement', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), 1, 'PensionDisbursement', 
     (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Pension Disbursement');
Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'Batch', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), '/pensionDisbursement/batchPensionDisbursement.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'PensionDisbursement'), 1, 'Batch', 1);
Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     ((select e.id_ROLE from eg_roles e where upper(e.ROLE_NAME) like 'SUPER%'), (select ID from eg_action where name like 'Batch'));

/****changes for aap config screen ****************/

	 
	 Insert into eg_module
     (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
   Values
     (SEQ_MODULEMASTER.nextval, 'Configuration', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Configuration', 
     (select ID_MODULE from eg_module where module_name like 'EIS-Payroll'), 'Configuration');
	 
	  Insert into eg_action
          (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
        Values
          (SEQ_EG_ACTION.nextval, 'CreateConfig', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), '/empPension/MasterSetUpAction.do', 'submitType=populateExistingDeatils\&mode=create', 1, (select ID_MODULE from eg_module where module_name like 'Configuration'), 1, 'Create', 1);
 
 
  Insert into eg_roleaction_map
          (ROLEID, ACTIONID)
        Values
     ((select e.id_ROLE from eg_roles e where upper(e.ROLE_NAME) like 'SUPER%'), (select ID from eg_action where name like 'CreateConfig'));
	 
	  Insert into eg_action
               (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
             Values
               (SEQ_EG_ACTION.nextval, 'ViewConfig', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), '/empPension/MasterSetUpAction.do', 'submitType=populateExistingDeatils\&mode=view', 1, (select ID_MODULE from eg_module where module_name like 'Configuration'), 1, 'View', 1);
       Insert into eg_roleaction_map
               (ROLEID, ACTIONID)
             Values
     ((select e.id_ROLE from eg_roles e where upper(e.ROLE_NAME) like 'SUPER%'), (select ID from eg_action where name like 'ViewConfig'));
     
 #DOWN
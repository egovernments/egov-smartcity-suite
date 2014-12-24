--supplier bill changes
 INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'Supplier Bill-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=beforeViewModify&mode=view', 32, 1, 'Supplier Bill-View', 0, '/HelpAssistance/AP/SuplierBill-View_AP.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'Supplier Bill-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=beforeViewModify&mode=modify', 32, 1, 'Supplier Bill-Modify', 0, '/HelpAssistance/AP/SuplierBill-Modify_AP.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'Supplier Bill-Create', TO_DATE('03/07/2009 12:24:15', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=beforeCreate', 32, 1, 'Create Supplier Bill', 1, '/HelpAssistance/AP/SuplierBill-Create_AP.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'Supplier Bill - Create', TO_DATE('03/07/2009 12:24:16', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=getTdsAndotherdtls&mode=create', 32, 1, 'Create Supplier Bill', 0, '/HelpAssistance/AP/SuplierBill-Create_AP.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL) 
 VALUES 
   (SEQ_EG_ACTION.NEXTVAL, ' Supplier Bill - Create', TO_DATE('03/07/2009 12:24:17', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=create', 32, 1, 'Create Supplier Bill', 0, '/HelpAssistance/AP/SuplierBill-Create_AP.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'Supplier Bill - Modify', TO_DATE('01/09/2009 16:26:12', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/supplierBill.do', 'submitType=getTdsAndotherdtls&mode=modify', 25, 1, 'Modify Supplier Bill', 0, '/HelpAssistance/AP/SuplierBill-Modify_AP.htm');


Insert into eg_object_type
  (ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE)
 Values
  (seq_object_type.nextVal, 'SuplierBill', 'Supplier Bill', sysdate);

UPDATE   eg_object_type 
SET description='Contractor Bill' WHERE TYPE='WorksBill';

INSERT INTO eg_roleaction_map   (SELECT (SELECT id_role FROM eg_roles WHERE role_name='SUPER USER'),id   FROM eg_action WHERE name LIKE '%Supplier Bill%') ;

UPDATE eg_action SET module_id=(SELECT id_module FROM eg_module WHERE module_name='Bill Registers'),Context_root='EGF' WHERE   name LIKE '%Supplier Bill%' AND url LIKE '/billsaccounting/supplierBill.do';
INSERT INTO eg_roleaction_map   (SELECT (SELECT id_role FROM eg_roles WHERE role_name='SuperUser'),id   FROM eg_action WHERE  name LIKE '%Supplier Bill%' AND url LIKE '/billsaccounting/supplierBill.do') ;
 
 UPDATE EG_ACTION SET url='/billsaccounting/purchaseBill.do' WHERE   url LIKE '/billsaccounting/supplierBill.do' ;
 UPDATE EG_ACTION SET name=REPLACE(name ,'Supplier','Purchase') ,  display_name =REPLACE(display_name,'Supplier','Purchase'),action_help_url=REPLACE(action_help_url,'Supplier','Purchase') WHERE  url='/billsaccounting/purchaseBill.do'
UPDATE EG_OBJECT_TYPE SET TYPE='PurchaseBill' ,description='Purchase Bill' WHERE TYPE='SupplierBill';

commit;

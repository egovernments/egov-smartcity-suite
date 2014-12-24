#UP
Insert into eg_module
(ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
Values
(SEQ_MODULEMASTER.nextval, 'Payhead Rule', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Payhead Rule',
(select ID_MODULE from eg_module where module_name like 'Payroll'), 'Payhead Rule');


Insert into eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
Values
(SEQ_EG_ACTION.nextval, 'CreatePayhead Rule', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'),
'/payhead/payheadRule.do', 'submitType=beforePayheadRule', 1, (select ID_MODULE from eg_module where module_name like 'Payhead Rule'), 1, 'Create', 1);


Insert into eg_roleaction_map
(ROLEID, ACTIONID)
Values
((select e.id_ROLE from eg_roles e where upper(e.ROLE_NAME) like 'SUPER%'), (select ID from eg_action where name like 'CreatePayhead Rule'));



Insert into eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
Values
(SEQ_EG_ACTION.nextval, 'viewPayheadRule', TO_DATE('12/11/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'),
'/payhead/searchPayheadRule.jsp', '', 1, (select ID_MODULE from eg_module where module_name like 'Payhead Rule'), 1, 'View', 1);


Insert into eg_roleaction_map
(ROLEID, ACTIONID)
Values
((select e.id_ROLE from eg_roles e where upper(e.ROLE_NAME) like 'SUPER%'), (select ID from eg_action where name like 'viewPayheadRule'));




#DOWN
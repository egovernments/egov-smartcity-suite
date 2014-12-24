#UP
INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES
(SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT ID FROM EG_APPCONFIG WHERE KEY_NAME='WORKORDER_ASSIGNEDTO_REQUIRED'),  SYSDATE, 'yes'); 
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT,START_DATE, END_DATE ) 
VALUES (EG_SCRIPT_SEQ.NEXTVAL,'workordernumber.for.workspackage','python',
'result=worksPackage.getUserDepartment().getDeptCode()+"/WO/"+worksPackage.getPackageNumberWithoutWP()',
TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));
#DOWN
delete from EG_APPCONFIG_VALUES where key_id (select id from EG_APPCONFIG where key_name='WORKORDER_ASSIGNEDTO_REQUIRED');
delete from EG_SCRIPT  where name='workordernumber.for.workspackage';

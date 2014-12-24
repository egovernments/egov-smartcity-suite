#UP

insert into EG_ROLEACTION_MAP (ROLEID,ACTIONID) VALUES((select id_role from eg_roles where role_name = 'Works User'),
(select id from eg_action where url = '/workflow/inbox.action' and name like 'WorkflowInbox'));

insert into EG_ROLEACTION_MAP (ROLEID,ACTIONID) VALUES((select id_role from eg_roles where role_name = 'Works User'),
(select id from eg_action where url = '/common/homepage!getAllModules.action' and name like 'EgiHomePageGetAllModules'));

delete from EG_WF_MATRIX where OBJECTTYPE='TenderFile';

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY,FROMDATE) 
values (EG_WF_MATRIX_SEQ.nextVal,'BR-Bus Route Roads','TenderFile','NEW',null,null,'ASSISTANT EXECUTIVE ENGINEER','noQuotation','Created','Pending for Approval','EXECUTIVE ENGINEER','CREATED','Forward,Cancel',null,null,TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY,FROMDATE) 
values (EG_WF_MATRIX_SEQ.nextVal,'BR-Bus Route Roads','TenderFile','Created',null,'Pending for Approval','EXECUTIVE ENGINEER',
'noQuotation','Approved','END',null,'APPROVED','Approve,Reject',null,null,TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY,FROMDATE) 
values (EG_WF_MATRIX_SEQ.nextVal,'BR-Bus Route Roads','TenderFile','Rejected',null,null,'ASSISTANT EXECUTIVE ENGINEER',
'noQuotation','Created','Pending for Approval','EXECUTIVE ENGINEER','RESUBMITTED','Forward,Cancel',null,null,TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

#DOWN

delete from EG_ROLEACTION_MAP where roleid = (select id_role from eg_roles where role_name = 'Works User')
and actionid = (select id from eg_action where url = '/workflow/inbox.action' and name like 'WorkflowInbox');
delete from EG_ROLEACTION_MAP where roleid = (select id_role from eg_roles where role_name = 'Works User')
and actionid = (select id from eg_action where url = '/common/homepage!getAllModules.action' and name like 'EgiHomePageGetAllModules');

delete from EG_WF_MATRIX where DEPARTMENT='Electrical' and OBJECTTYPE='TenderFile';
Insert into CHARTOFACCOUNTS (ID,GLCODE,NAME,DESCRIPTION,ISACTIVEFORPOSTING,PARENTID,LASTMODIFIED,MODIFIEDBY,CREATED,PURPOSEID,OPERATION,
TYPE,CLASS,CLASSIFICATION,FUNCTIONREQD,BUDGETCHECKREQ,SCHEDULEID,RECEIPTSCHEDULEID,RECEIPTOPERATION,PAYMENTSCHEDULEID,PAYMENTOPERATION,
MAJORCODE,CREATEDBY,FIESCHEDULEID,FIEOPERATION) 
values (nextval('seq_chartofaccounts'),'1','Income',null,false,null,current_date,1,current_date,null,null,'I',null,0,false,null,null,null,null,null,null,'1',null,null,null);
Insert into CHARTOFACCOUNTS (ID,GLCODE,NAME,DESCRIPTION,ISACTIVEFORPOSTING,PARENTID,LASTMODIFIED,MODIFIEDBY,CREATED,PURPOSEID,OPERATION,
TYPE,CLASS,CLASSIFICATION,FUNCTIONREQD,BUDGETCHECKREQ,SCHEDULEID,RECEIPTSCHEDULEID,RECEIPTOPERATION,PAYMENTSCHEDULEID,PAYMENTOPERATION,
MAJORCODE,CREATEDBY,FIESCHEDULEID,FIEOPERATION) 
values (nextval('seq_chartofaccounts'),'2','Expenses',null,false,null,current_date,1,current_date,null,null,'E',null,0,false,null,null,null,null,null,null,'2',null,null,null);
Insert into CHARTOFACCOUNTS (ID,GLCODE,NAME,DESCRIPTION,ISACTIVEFORPOSTING,PARENTID,LASTMODIFIED,MODIFIEDBY,CREATED,PURPOSEID,OPERATION,
TYPE,CLASS,CLASSIFICATION,FUNCTIONREQD,BUDGETCHECKREQ,SCHEDULEID,RECEIPTSCHEDULEID,RECEIPTOPERATION,PAYMENTSCHEDULEID,PAYMENTOPERATION,
MAJORCODE,CREATEDBY,FIESCHEDULEID,FIEOPERATION) 
values (nextval('seq_chartofaccounts'),'4','Assets',null,false,null,current_date,1,current_date,null,null,'A',null,0,false,null,null,null,null,null,null,'4',null,null,null);
Insert into CHARTOFACCOUNTS (ID,GLCODE,NAME,DESCRIPTION,ISACTIVEFORPOSTING,PARENTID,LASTMODIFIED,MODIFIEDBY,CREATED,PURPOSEID,OPERATION,
TYPE,CLASS,CLASSIFICATION,FUNCTIONREQD,BUDGETCHECKREQ,SCHEDULEID,RECEIPTSCHEDULEID,RECEIPTOPERATION,PAYMENTSCHEDULEID,PAYMENTOPERATION,
MAJORCODE,CREATEDBY,FIESCHEDULEID,FIEOPERATION) 
values (nextval('seq_chartofaccounts'),'3','Liabilities',null,false,null,current_date,1,current_date,null,null,'L',null,0,false,null,null,null,null,null,null,'3',null,null,null);

update chartofaccounts set parentid=(select id from chartofaccounts where glcode='1') where glcode like '1%' and parentid is null and classification=1;
update chartofaccounts set parentid=(select id from chartofaccounts where glcode='2') where glcode like '2%' and parentid is null and classification=1;
update chartofaccounts set parentid=(select id from chartofaccounts where glcode='3') where glcode like '3%' and parentid is null and classification=1;
update chartofaccounts set parentid=(select id from chartofaccounts where glcode='4') where glcode like '4%' and parentid is null and classification=1;

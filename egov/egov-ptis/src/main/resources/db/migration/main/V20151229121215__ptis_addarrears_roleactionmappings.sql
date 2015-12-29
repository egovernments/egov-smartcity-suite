INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'addArrearsForm','/addarrears/form/', null,(select id from EG_MODULE where name = 'Existing property'),1,
'addArrearsForm','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'addArrearsForm'), 
(Select id from eg_role where name='ULB Operator'));



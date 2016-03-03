Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeAssignmentSearchRTGS','/payment/chequeAssignment-searchRTGS.action',null,(select id from eg_module where name='Payments'),1,'ChequeAssignmentSearchRTGS',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChequeAssignmentSearchRTGS'));


Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ChequeAssignmentUpdate','/payment/chequeAssignment-update.action',null,(select id from eg_module where name='Payments'),1,'ChequeAssignmentUpdate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ChequeAssignmentUpdate'));


INSERT INTO EGF_INSTRUMENTTYPE (ID,TYPE,ISACTIVE,CREATEDBY,LASTMODIFIEDBY,CREATEDDATE,LASTMODIFIEDDATE) values (nextval('SEQ_EGF_INSTRUMENTTYPE'),'advice','1',1,1, current_timestamp, current_timestamp);


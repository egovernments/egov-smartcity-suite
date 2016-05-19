Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SubmitAllReceiptCollection','/receipts/collectionsWorkflow-submitAllCollections.action',null,(select id from eg_module where name='Receipt Services'),1,'SubmitAllReceiptCollection',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='SubmitAllReceiptCollection'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'CollectionsWorkflowApproveAll','/receipts/collectionsWorkflow-approveAllCollections.action',null,(select id from eg_module where name='Receipt Services'),1,'CollectionsWorkflowApproveAll',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='CollectionsWorkflowApproveAll'));

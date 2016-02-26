ALTER TABLE egf_account_cheques ALTER isexhausted TYPE bool USING CASE WHEN isexhausted='0' THEN FALSE ELSE TRUE END;

Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AccountChequeSave','/masters/accountCheque-save.action',null,(select id from eg_module where name='Chart of Accounts'),1,'AccountChequeSave',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AccountChequeSave'));


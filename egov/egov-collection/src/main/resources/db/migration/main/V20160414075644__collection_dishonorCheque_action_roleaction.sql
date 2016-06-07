Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'DishonoredChequeProcess','/receipts/dishonoredCheque-process.action',null,(select id from eg_module where name='Collection Transaction'),1,'DishonoredChequeProcess',false,'collection',0,1,current_date,1,current_date,(select id from eg_module where name = 'Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'DishonoredChequeProcess' and contextroot='collection'));


update eg_action set url = '/receipts/dishonoredCheque-create.action' where name ='DishonoredChequeSubmit';





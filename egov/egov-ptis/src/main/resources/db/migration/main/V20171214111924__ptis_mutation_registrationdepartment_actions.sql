update eg_action set name = 'RegisteredTransfer-Form', displayname = 'Transfer of title', url = '/search/searchproperty-registeredtransfer' where name = 'TransferOwnership-Form';


INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'FullTransfer-Form','/search/searchproperty-fulltransfer',null,
(select id from eg_module where name='Existing property'),null,'Transfer of title (For Registration)',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'FullTransfer-payment-redirect','/property/transfer/redirectForPayment',null,
(select id from eg_module where name='Existing property'),null,'FullTransfer-payment-redirect',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


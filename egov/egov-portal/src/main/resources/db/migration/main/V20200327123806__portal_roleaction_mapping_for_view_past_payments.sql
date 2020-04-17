
----role action mapping for view past payments

insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,
displayname,enabled,contextroot,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values
(nextval('seq_eg_action'),'ViewPastPayments','/pastpayments/view',null,(select id from eg_module where name='View Past Payments' and contextroot='portal'),3,'View Past Payments',false,'portal',(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),(select id from eg_module where name='Citizen Portal'));

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CITIZEN'),(select id from eg_action where name='ViewPastPayments'));

insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,
displayname,enabled,contextroot,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values
(nextval('seq_eg_action'),'SearchPastPayments','/pastpayments/search',null,(select id from eg_module where name='View Past Payments' and contextroot='portal'),4,'Search Past Payments',false,'portal',(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),(select id from eg_module where name='Citizen Portal'));

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CITIZEN'),(select id from eg_action where name='SearchPastPayments'));



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Payment Search','/payment/payment-search.action',null,(select id from eg_module where name='Payments'),1,'Payment Search',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Payment Search'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Payment Search'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Payment Save','/payment/payment-save.action',null,(select id from eg_module where name='Payments'),1,'Payment Save',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Payment Save'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Payment Save'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Payment Create','/payment/payment-create.action',null,(select id from eg_module where name='Payments'),1,'Payment Create',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Payment Create'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Payment Create'));


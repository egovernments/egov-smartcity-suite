INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'calculateMutationFees','/common/ajaxCommon-calculateMutationFee.action',null,(select id from eg_module  where name='Existing property'),1,'calculateMutationFees',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'calculateMutationFees'),id from eg_role where name in ('ULB Operator','MeeSeva Operator');


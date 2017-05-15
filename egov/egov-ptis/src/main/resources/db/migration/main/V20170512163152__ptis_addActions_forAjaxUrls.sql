-- Adding action isApputunant

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'CheckIsAppurtenant','/ptis/common/ajaxCommon-isAppurTenant.action',null,
(select id from eg_module where name='New Property'),1,'Is Appurtenant',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

-- Adding action getUserByMobile

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'UserDetailsByMobileNumber','/ptis/common/ajaxCommon-getUserByMobileNo.action',null,
(select id from eg_module where name='Existing property'),1,'Get User By Mobile No',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


-- Adding action calculateMutationfee

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'CalculateMutationFee','/ptis/common/ajaxCommon-calculateMutationFee.action',null,
(select id from eg_module where name='Existing property'),1,'Calculate Mitation Fee',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


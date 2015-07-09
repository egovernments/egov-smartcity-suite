INSERT INTO EG_SCRIPT VALUES(nextval('SEQ_EG_SCRIPT'),'PTIS-MUTATION-FEE-CALCULATOR','nashorn',1,now(),1,now(),'
if(transferReason === "Blood Relation")
  result = (0.1/100)*marketValue;
else if(transferedInMonths <= 3) 
  result = (0.5/100)*marketValue;
else if (transferedInMonths > 3)
  result = (0.55/100)*marketValue;',now(),'01-Jan-2100',0);
Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Calculate Mutation Fee','/property/transfer/calculate-mutationfee.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Calculate Mutation Fee', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Calculate Mutation Fee'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='Calculate Mutation Fee'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('SEQ_EG_ACTION'),'modifyPropertyAckPrint','/modify/modifyProperty-printAck', null, (select ID from eg_module where NAME ='New Property'), null, 'modifyPropertyAckPrint', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator'), (select id from eg_action where name='modifyPropertyAckPrint'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name='modifyPropertyAckPrint'));

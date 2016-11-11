INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Update ImplementaionStatus','/councilpreamble/updateimplimentaionstatus',(select id from eg_module where name='Council Preamble' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),12,'Update ImplementaionStatus',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update ImplementaionStatus'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Creator'),(select id from eg_action where name='Update ImplementaionStatus'));

update eg_action set enabled='true' where name='Search and Edit-CouncilPreamble' and contextroot='council';
update eg_action set displayname='Update Preamble Status' where name='Search and Edit-CouncilPreamble' and contextroot='council';

INSERT into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','Resolution Approved',now(),'Resolution Approved',8);
INSERT into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'IMPLEMENTATIONSTATUS','Work In Progress',now(),'Work In Progress',1);
INSERT into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'IMPLEMENTATIONSTATUS','Finished',now(),'Finished',2);
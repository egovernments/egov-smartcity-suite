
----------------------------------Capture Attendance Details Role Action Mapping----------------------

update eg_action set displayname ='Enter Attendance' where url='/councilmeeting/attendance/search' and contextroot='council';

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit Attendance','/councilmeeting/attendance/search/edit/',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Edit Attendance',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Edit Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Edit Attendance'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Finalize Attendance','/councilmeeting/attendance/finalizeattendance',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Finalize Attendance',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Finalize Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Finalize Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Finalize Attendance'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Attendance','/councilmeeting/attendance/update',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Update Attendance',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Update Attendance'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Update Attendance'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Show Attendance Result','/councilmeeting/attendance/result/',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),6,'Show Attendance Result',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Show Attendance Result'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Show Attendance Result'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Show Attendance Result'));

--------------------------Status For To Finalize Attendance---------------

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILMEETING','MEETING_ATTENDANCE FINALIZED',now(),'ATTENDANCE FINALIZED',5);

-------------------------Capture Attendance Details feature action mapping -----------------------

update eg_feature set description ='Enter Meeting Attendance' where name='Search Meeting Attendance' and module=(select id from eg_module  where name = 'Council Management');


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit Attendance') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Finalize Attendance') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Attendance') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Show Attendance Result') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));
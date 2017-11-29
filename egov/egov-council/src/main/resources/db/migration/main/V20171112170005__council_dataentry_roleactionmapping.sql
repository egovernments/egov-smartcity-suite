INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'councilmeeting-datentry','/councilmom/createdataentry',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Council Data Entry Screen',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='councilmeeting-datentry'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='councilmeeting-datentry'));


INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'savecouncil-datentry','/councilmom/savedataentry',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Save Council Data Entry Screen',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='savecouncil-datentry'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='savecouncil-datentry'));


INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'checkUnqMeetingNumber','/councilmom/checkUnique-MeetingNo',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Unique Meeting number check',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='checkUnqMeetingNumber'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='checkUnqMeetingNumber'));


INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'checkUnqPreambleNumber','/councilmom/checkUnique-preambleNo',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Unique Preamble number check',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='checkUnqPreambleNumber'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='checkUnqPreambleNumber'));


INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'checkUnqResolutionNumber','/councilmom/checkUnique-resolutionNo',(select id from eg_module where name='Council Management Transaction' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Unique Resolution number check',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
INSERT INTO eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='checkUnqResolutionNumber'));
INSERT INTO eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='checkUnqResolutionNumber'));


ALTER table egcncl_agenda  alter agendanumber  drop not null;

ALTER TABLE egcncl_meeting_mom ADD COLUMN islegacy boolean DEFAULT false; 




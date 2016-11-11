
CREATE SEQUENCE seq_egcncl_committee_members;

CREATE TABLE egcncl_committee_members
(
  id bigint NOT NULL,
  committeetype bigint NOT NULL,
  councilmember bigint NOT NULL,
  createddate timestamp without time zone,
  createdby bigint,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_egcncl_committee_members PRIMARY KEY (id),
  CONSTRAINT fk_egcncl_committeetype FOREIGN KEY (committeetype)
      REFERENCES egcncl_committeetype (id) ,
      CONSTRAINT fk_egcncl_members FOREIGN KEY (councilmember)
      REFERENCES egcncl_members (id)
);

ALTER TABLE egcncl_meeting_attendence DROP CONSTRAINT fk_egcncl_meetingmember;
ALTER TABLE egcncl_meeting_attendence RENAME COLUMN councilmember TO committeemember;

ALTER TABLE egcncl_meeting_attendence
    ADD CONSTRAINT fk_egcncl_meetingmember FOREIGN KEY (committeemember) REFERENCES egcncl_committee_members(id);


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'SendSmsAndEmailForMeeting','/councilmeeting/sendsmsemail',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'SendSmsAndEmailForMeeting',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SendSmsAndEmailForMeeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'RetrieveSmsAndEmailForMeeting','/councilmeeting/viewsmsemail',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),5,'Send Sms And Email',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='RetrieveSmsAndEmailForMeeting'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'SearchAttendanceForMeeting','/councilmeeting/attendance/search',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),6,'Search Attendance',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchAttendanceForMeeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'ShowAttendanceSearchResult','/councilmeeting/attendance/search/view/',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'ShowAttendanceSearchResult',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ShowAttendanceSearchResult'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'AttendanceAjaxSearch','/councilmeeting/attendance/ajaxsearch/',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'AttendanceAjaxSearch',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AttendanceAjaxSearch'));

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','PREAMBLE USED IN AGENDA',now(),'PREAMBLE USED IN AGENDA',6);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILAGENDA','AGENDA USED IN MEETING',now(),'AGENDA USED IN MEETING',1);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILMEETING','MOM CREATED',now(),'MOM CREATED',1);
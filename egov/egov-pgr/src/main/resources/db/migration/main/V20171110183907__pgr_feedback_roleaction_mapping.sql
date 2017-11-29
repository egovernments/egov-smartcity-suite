
create sequence SEQ_EGPGR_FEEDBACK_REASON;

CREATE TABLE EGPGR_FEEDBACK_REASON
(
  id bigint NOT NULL,
  name character varying(128) NOT NULL,
  code character varying(5) NOT NULL,
  description character varying(256), 
  version bigint DEFAULT 0,
  CONSTRAINT pk_egpgr_feedback_reason PRIMARY KEY (id),
  CONSTRAINT unq_pgrfeebackreason_code UNIQUE (code),
  CONSTRAINT unq_pgrfeebackreason_name UNIQUE (name)
);


create sequence SEQ_EGPGR_QUALITYREVIEW;

CREATE TABLE EGPGR_QUALITYREVIEW
(
  id bigint NOT NULL,
  complaint bigint Not Null,
  feedbackReason bigint Not Null,
  rating bigint ,
  detail character varying(128), 
  feedbackDate timestamp without time zone NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  CONSTRAINT pk_EGPGR_QUALITYREVIEW PRIMARY KEY (id),
  CONSTRAINT fk_egpgr_feedback_reason FOREIGN KEY (feedbackReason)
      REFERENCES EGPGR_FEEDBACK_REASON (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_egpgr_complaint FOREIGN KEY (complaint)
  	REFERENCES egpgr_complaint (id) MATCH SIMPLE
  	ON UPDATE NO ACTION ON DELETE NO ACTION
);

--feedback master
insert into eg_module values(nextval('seq_eg_module'),'Feedback',true,'pgr',(select id from eg_module where name='Pgr Masters'),'Feedback',6);

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Feedback Reason',
'/complaint/feedbackreason/create',null,(select id from EG_MODULE where name = 'Feedback'),1,
'Create Feedback Reason','t','pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='Feedback Reason'),
(select id from eg_role where name='SYSTEM'));

insert into egpgr_feedback_reason (id,name,code,description,version) values
(NEXTVAL('SEQ_EGPGR_FEEDBACK_REASON'),'Delay in resolving complaint','00001','Delay in resolving complaint',0);

insert into egpgr_feedback_reason (id,name,code,description,version) values
(NEXTVAL('SEQ_EGPGR_FEEDBACK_REASON'),'Complaint closed without resolution','00002','Complaint closed without resolution',0);

insert into egpgr_feedback_reason (id,name,code,description,version) values
(NEXTVAL('SEQ_EGPGR_FEEDBACK_REASON'),'Complaint addressed partially','00003','Complaint addressed partially',0);

insert into egpgr_feedback_reason (id,name,code,description,version) values
(NEXTVAL('SEQ_EGPGR_FEEDBACK_REASON'),'Others','00004','Others',0);


--search rated grievcance

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search Rated Grievance',
'/qualityreview/search',null,(select id from EG_MODULE where name = 'PGRComplaints'),1,
'Grievance Quality Review','t','pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='Search Rated Grievance'),
(select id from eg_role where name='SYSTEM'));


-- capture feedback
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Complaint Quality Review',
'/complaint/qualityreview',null,(select id from EG_MODULE where name = 'PGRComplaints'),1,
'ComplaintQualityReview','f','pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='Complaint Quality Review'),
(select id from eg_role where name='SYSTEM'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Complaint Type Search By Department',
'/complainttype/search/by-department',null,(select id from EG_MODULE where name = 'Complaint Type'),1,
'Complaint Type Search By Department','f','pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='Complaint Type Search By Department'),
(select id from eg_role where name='SYSTEM'));

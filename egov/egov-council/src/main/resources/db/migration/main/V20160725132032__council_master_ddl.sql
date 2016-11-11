create table egcncl_preamble(
id bigint,
department  bigint not null,
PreambleNumber varchar(25) not null,
type varchar(25) not null,
sanctionAmount bigint ,
gistOfPreamble  varchar(5000) not null,
filestoreid bigint, 
status bigint not null,
state_id bigint,
createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
lastmodifieddate timestamp without time zone,
createdby bigint NOT NULL,
lastmodifiedby bigint,
version bigint DEFAULT 0
);

ALTER TABLE egcncl_preamble ADD CONSTRAINT fk_egcncl_preambleState FOREIGN KEY (STATE_ID)  REFERENCES eg_wf_states (id) ;
ALTER TABLE egcncl_preamble ADD CONSTRAINT fk_egcncl_Preambledept FOREIGN KEY (department)  REFERENCES eg_department (id) ;
ALTER TABLE egcncl_preamble ADD CONSTRAINT fk_egcncl_Preamblstatus FOREIGN KEY (status)  REFERENCES egw_status (id) ;
alter table egcncl_preamble add constraint pk_egcncl_preamble primary key (id);

create sequence seq_egcncl_preamble;


create table egcncl_committeeType(
id bigint,
name varchar(100) NOT NULL,
code varchar(20) NOT NULL,
isActive boolean NOT NULL default true,
createddate timestamp without time zone,
createdby bigint,
lastmodifieddate timestamp without time zone,
lastmodifiedby bigint,
version bigint DEFAULT 0
);

alter table egcncl_committeeType add constraint pk_egcncl_committeeType primary key (id);
create sequence seq_egcncl_committeeType;

create table egcncl_agenda(
id bigint,
committeeType bigint not null,
agendaNumber varchar(25) not null,
status bigint not null,
state_id bigint,
createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
lastmodifieddate timestamp without time zone,
createdby bigint NOT NULL,
lastmodifiedby bigint,
version bigint DEFAULT 0
);

ALTER TABLE egcncl_agenda ADD CONSTRAINT fk_egcncl_agendaState FOREIGN KEY (STATE_ID)  REFERENCES eg_wf_states (id) ;
ALTER TABLE egcncl_agenda ADD CONSTRAINT fk_egcncl_agendastatus FOREIGN KEY (status)  REFERENCES egw_status (id) ;
alter table egcncl_agenda add constraint pk_egcncl_agenda primary key (id);
alter table egcncl_agenda add constraint fk_egcncl_agendacommitype FOREIGN KEY (committeeType)  REFERENCES egcncl_committeeType(id);
create sequence seq_egcncl_agenda;


create table egcncl_agenda_details(
id bigint,
agenda bigint not null,
itemnumber varchar(25) not null,
order_id bigint,
preamble bigint not null
);

alter table egcncl_agenda_details add constraint pk_egcncl_agendadtl primary key (id);
ALTER TABLE egcncl_agenda_details ADD CONSTRAINT fk_egcncl_agendadtlagenda FOREIGN KEY (agenda)  REFERENCES egcncl_agenda(id);
ALTER TABLE egcncl_agenda_details ADD CONSTRAINT fk_egcncl_agendadtlpreamble FOREIGN KEY (preamble)  REFERENCES egcncl_preamble(id);
create sequence seq_egcncl_agenda_details;

create table egcncl_meeting (
id bigint,
committeeType bigint not null,
meetingNumber varchar(25) not null,
meetingDate timestamp without time zone,
meetingTime varchar(50),
meetingLocation varchar(100),
status bigint not null,
state_id bigint,
createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
lastmodifieddate timestamp without time zone,
createdby bigint NOT NULL,
lastmodifiedby bigint,
version bigint DEFAULT 0
);

ALTER TABLE egcncl_meeting ADD CONSTRAINT fk_egcncl_meetingaState FOREIGN KEY (STATE_ID)  REFERENCES eg_wf_states (id) ;
ALTER TABLE egcncl_meeting ADD CONSTRAINT fk_egcncl_meetingstatus FOREIGN KEY (status)  REFERENCES egw_status (id) ;
alter table egcncl_meeting add constraint pk_egcncl_meeting primary key (id);
alter table egcncl_meeting add constraint fk_egcncl_meeting_commType  FOREIGN KEY (committeeType)  REFERENCES egcncl_committeeType(id);
create sequence seq_egcncl_meeting;



create table egcncl_meeting_attendence (
id bigint,
meeting bigint not null,
councilmember bigint not null,
attendedMeeting  boolean NOT NULL default false,
createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
lastmodifieddate timestamp without time zone,
createdby bigint NOT NULL,
lastmodifiedby bigint,
version bigint DEFAULT 0
);

alter table egcncl_meeting_attendence add constraint pk_egcncl_meetingAttndce primary key (id);
alter table egcncl_meeting_attendence add constraint fk_egcncl_meetingattdnce FOREIGN KEY (meeting)  REFERENCES egcncl_meeting (id) ;
alter table egcncl_meeting_attendence add constraint fk_egcncl_meetingMember FOREIGN KEY (councilmember)  REFERENCES egcncl_members(id) ;
create sequence seq_egcncl_meetingAttendence;

create table egcncl_meeting_mom(
id bigint,
agenda bigint ,
preamble bigint not null,
resolutionDetail  varchar(5000) not null,
resolutionstatus bigint,
resolutionNumber  varchar(25)
);
alter table egcncl_meeting_mom add constraint pk_egcncl_meetingMOM primary key (id);
alter table egcncl_meeting_mom add constraint fk_egcncl_meetingmom_agenda  FOREIGN KEY (agenda)  REFERENCES egcncl_agenda(id);
alter table egcncl_meeting_mom add constraint fk_egcncl_meetngmom_premble  FOREIGN KEY (preamble)  REFERENCES egcncl_preamble(id);
ALTER TABLE egcncl_meeting_mom ADD CONSTRAINT fk_egcncl_meetingmomstatus FOREIGN KEY (resolutionstatus)  REFERENCES egw_status (id) ;
create sequence seq_egcncl_meetingMom;


Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','CREATED',now(),'CREATED',1);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','APPROVAL PENDING',now(),'PENDINGAPPROVAL',2);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','APPROVED',now(),'APPROVED',3);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','REJECTED',now(),'REJECTED',4);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','Used in Agenda',now(),'INWORKFLOW',5);

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILRESOLUTION','APPROVED',now(),'APPROVED',1);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILRESOLUTION','ADJOURNED',now(),'ADJOURNED',2);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILRESOLUTION','REJECTED',now(),'REJECTED',3);


CREATE TABLE egcncl_meetingtype
(
  id bigint NOT NULL,
  name character varying(30) NOT NULL,
  code varchar(20) NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  isActive boolean NOT NULL default true,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_eg_meetingtype PRIMARY KEY (id)
);

create sequence seq_egcncl_meetingtype;


insert into egcncl_meetingtype(id,name,code,isActive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(
    nextval('seq_egcncl_meetingtype'),'Ordinary Meeting','ORD',true,now(),1,now(),1,0);
insert into egcncl_meetingtype(id,name,code,isActive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(
    nextval('seq_egcncl_meetingtype'),'Urgent Meeting','URG',true,now(),1,now(),1,0);
insert into egcncl_meetingtype(id,name,code,isActive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(
    nextval('seq_egcncl_meetingtype'),'Special Meeting','SPL',true,now(),1,now(),1,0);
insert into egcncl_meetingtype(id,name,code,isActive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(
    nextval('seq_egcncl_meetingtype'),'Requisition Meeting','REQ',true,now(),1,now(),1,0);
    
alter table egcncl_meeting ADD COLUMN meetingtype bigint not null default 1;
alter table egcncl_meeting add constraint fk_egcncl_meeting_type  FOREIGN KEY (meetingType)  REFERENCES egcncl_meetingtype(id);
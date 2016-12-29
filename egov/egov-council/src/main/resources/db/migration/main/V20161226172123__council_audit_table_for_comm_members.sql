insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(nextval('seq_egcncl_designation'),'Co-Option',true,now(),1,now(),1,0,'Co-Option');

insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(nextval('seq_egcncl_designation'),'Special Officer',true,now(),1,now(),1,0,'Special Officer');

insert into egcncl_committeetype(id,name,code,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(nextval('seq_egcncl_committeeType'),'HM panel Committee','HM',true,now(),1,now(),1,0);


 CREATE TABLE egcncl_committee_members_aud
(
  id integer not null,
  rev integer not null,
  committeetype bigint NOT NULL,
  councilmember bigint NOT NULL,
  revtype numeric,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egcncl_committee_members_aud ADD CONSTRAINT pk_egcncl_committee_members_aud PRIMARY KEY (id, rev);

ALTER TABLE egcncl_members  ADD COLUMN category character varying(25);
ALTER TABLE egcncl_members  ADD COLUMN dateofjoining date;

ALTER TABLE egcncl_meeting_attendence DROP CONSTRAINT fk_egcncl_meetingmember;
update egcncl_meeting_attendence meetingatt set committeemember=(select councilmember from egcncl_committee_members member  where  member.id=meetingatt.committeemember);
ALTER TABLE egcncl_meeting_attendence ADD CONSTRAINT fk_egcncl_meetingmember FOREIGN KEY (committeemember) REFERENCES egcncl_members (id) ;
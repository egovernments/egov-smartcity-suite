alter table egcncl_members add column status  bigint NOT NULL;
alter table egcncl_designation add column code character varying(20) NOT NULL;
alter table egcncl_qualification add column code character varying(20) NOT NULL;
alter table egcncl_party add column code character varying(20) NOT NULL;
alter table egcncl_caste add column code character varying(20) NOT NULL;
alter table egpt_property_mutation add id_registration_details bigint;

create table egpt_mutation_registration_details (
  id bigint primary key,
  seller character varying(64),
  buyer character varying(64),
  typeoftransfer character varying(64),
  documentno character varying(64),
  documentdate date,
  partyvalue double precision,
  departmentvalue double precision,
  sroname character varying(128),
  doorno character varying(64),
  address character varying(128),
  eastboundary character varying(128),
  westboundary character varying(128),
  northboundary character varying(128),
  southboundary character varying(128),
  plintharea double precision,
  plotarea double precision,
  documentvalue double precision
);

alter table egpt_property_mutation add constraint fk_id_registration_details foreign key (id_registration_details) references egpt_mutation_registration_details (id);

create sequence seq_egpt_mutation_registration_details;

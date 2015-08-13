create table egwtr_existing_connection_details
(
  id bigint not null,
  monthlyfee  double precision,
  connectiondetailsid bigint not null,
  donationcharges  double precision,
  arrears double precision,
  metercost double precision,
  metername character varying(20),
  meterno character varying(20),
  previousreading bigint,
  currentreading bigint,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric default 0,
  constraint pk_existing_connection_details primary key (id),
  constraint fk_existing_connection_details_details foreign key (connectiondetailsid)
      references egwtr_connectiondetails (id),
       CONSTRAINT fk_existing_connection_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id) ,
     CONSTRAINT fk_existing_connection_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

create sequence seq_egwtr_existing_connection_details;

--rollback drop sequence seq_egwtr_existing_connection_details;
--rollback drop table egwtr_existing_connection_details;
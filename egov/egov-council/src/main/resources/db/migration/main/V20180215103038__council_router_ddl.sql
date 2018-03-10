
CREATE TABLE egcncl_router
(
  id bigint NOT NULL,
  department bigint ,
  type integer ,
  position bigint,
  createddate timestamp without time zone NOT NULL DEFAULT ('now'::text)::date,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint DEFAULT 0
);
CREATE SEQUENCE seq_egcncl_router;

alter table egcncl_router add constraint pk_egcncl_router primary key (id);
alter table egcncl_router add constraint fk_eg_department FOREIGN KEY (department)  REFERENCES eg_department (id) ;
alter table egcncl_router add constraint fk_position FOREIGN KEY (position)  REFERENCES eg_position (id) ;

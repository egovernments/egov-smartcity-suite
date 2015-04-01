CREATE TABLE pgr_router
(
  id bigint NOT NULL,
  complainttypeid numeric,
  position bigint,
  bndryid bigint,
  version bigint,
  CONSTRAINT pk_pgr_router_id PRIMARY KEY (id),
  CONSTRAINT fk_pgr_router_complainttypeid FOREIGN KEY (complainttypeid) REFERENCES pgr_complainttype (id),
  CONSTRAINT fk_pgr_router_position FOREIGN KEY (position) REFERENCES eg_position (id),
  CONSTRAINT fk_pgr_router_bndryid FOREIGN KEY (bndryid) REFERENCES eg_boundary (id_bndry)
);

--rollback DROP TABLE pgr_router;


CREATE SEQUENCE seq_pgr_router;

--rollback DROP SEQUENCE seq_pgr_router;




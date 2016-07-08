------------------START------------------
CREATE TABLE egw_lineestimate_uom
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  CONSTRAINT pk_lineestimate_uom PRIMARY KEY (id)
);

CREATE SEQUENCE SEQ_EGW_LINEESTIMATE_UOM;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_mode_of_allotment
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  CONSTRAINT pk_mode_of_allotment PRIMARY KEY (id)
);

CREATE SEQUENCE SEQ_EGW_MODE_OF_ALLOTMENT;
-------------------END-------------------

--rollback DROP SEQUENCE SEQ_egw_mode_of_allotment;
--rollback DROP TABLE egw_mode_of_allotment;

--rollback DROP SEQUENCE SEQ_egw_lineestimate_uom;
--rollback DROP TABLE egw_lineestimate_uom;
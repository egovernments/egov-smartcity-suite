CREATE TABLE egpt_appartments
(
 id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_appartments PRIMARY KEY(id),
 CONSTRAINT fk_appartment_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_appartment_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);

ALTER TABLE egpt_floor_detail ADD COLUMN occupancydate TIMESTAMP without time zone;
ALTER TABLE egpt_floor_detail ADD COLUMN occupantname character varying(32);

--rollback DROP TABLE egpt_appartments;
--rollback ALTER TABLE egpt_floor_detail DROP COLUMN occupancydate;
--rollback ALTER TABLE egpt_floor_detail DROP COLUMN occupantname;

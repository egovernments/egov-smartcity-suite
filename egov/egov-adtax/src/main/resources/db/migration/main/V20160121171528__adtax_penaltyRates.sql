
----------Start ------------

CREATE TABLE EGADTAX_MSTR_PENALTYRATES (
 id bigint NOT NULL,
 rangefrom double precision NOT NULL DEFAULT 0,
 rangeto double precision NOT NULL,
 percentage double precision NOT NULL DEFAULT 0,
 version bigint DEFAULT 0
);

CREATE SEQUENCE SEQ_EGADTAX_PENALTYRATES;

ALTER TABLE ONLY EGADTAX_MSTR_PENALTYRATES ADD CONSTRAINT pk_adtax_penaltyrates PRIMARY KEY (id);
CREATE INDEX idx_adtax_penalty_rangefrom ON EGADTAX_MSTR_PENALTYRATES USING btree (rangefrom);
CREATE INDEX idx_adtax_penalty_rangeto ON EGADTAX_MSTR_PENALTYRATES USING btree (rangeto);

------- end --------------

---- Default penalty rates ---------

insert into EGADTAX_MSTR_PENALTYRATES(id,rangefrom,rangeto,percentage) VALUES (nextval('SEQ_EGADTAX_PENALTYRATES'), -999999,60,0);
insert into EGADTAX_MSTR_PENALTYRATES(id,rangefrom,rangeto,percentage) VALUES (nextval('SEQ_EGADTAX_PENALTYRATES'), 60,120,25);
insert into EGADTAX_MSTR_PENALTYRATES(id,rangefrom,rangeto,percentage) VALUES (nextval('SEQ_EGADTAX_PENALTYRATES'), 120,999999,50);
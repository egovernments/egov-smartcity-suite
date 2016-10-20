CREATE TABLE egcl_dishonorcheque (
    id bigint NOT NULL,
    collectionheader bigint NOT NULL,
    instrumentheader bigint NOT NULL,
    status bigint NOT NULL,
    bankcharges double precision,
    transactiondate timestamp without time zone NOT NULL,
    bankchargeschartofaccounts bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    bankreferencenumber character varying(20),
    reversalvoucher bigint,
    bankchargesvoucher bigint,
    state_id bigint,
    bankreason character varying(50),
    instrumentdishonorreason character varying(50),
    version numeric
);

CREATE SEQUENCE seq_egcl_dishonorcheque;

ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT pk_egcl_dishonorcheque PRIMARY KEY (id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_glcode FOREIGN KEY (bankchargeschartofaccounts) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT egcl_dishcq_bcvh FOREIGN KEY (bankchargesvoucher) REFERENCES voucherheader(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_insthead FOREIGN KEY (instrumentheader) REFERENCES egf_instrumentheader(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_lastmodby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_rvh FOREIGN KEY (reversalvoucher) REFERENCES voucherheader(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_stat FOREIGN KEY (status) REFERENCES egw_status(id);
ALTER TABLE ONLY egcl_dishonorcheque
    ADD CONSTRAINT fk_egcl_dishchq_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
    
CREATE INDEX idx_egcl_dishonorcheque_collhead ON egcl_dishonorcheque USING btree (collectionheader);
CREATE INDEX idx_egcl_dishonorcheque_bnkchrg ON egcl_dishonorcheque USING btree (bankchargeschartofaccounts);
CREATE INDEX idx_egcl_dishonorcheque_bnkchrgvchr ON egcl_dishonorcheque USING btree (bankchargesvoucher);
CREATE INDEX idx_egcl_dishonorcheque_crtdby ON egcl_dishonorcheque USING btree (createdby);
CREATE INDEX idx_egcl_dishonorcheque_insthead ON egcl_dishonorcheque USING btree (instrumentheader);
CREATE INDEX idx_egcl_dishonorcheque_lastmodby ON egcl_dishonorcheque USING btree (lastmodifiedby);
CREATE INDEX idx_egcl_dishonorcheque_revchr ON egcl_dishonorcheque USING btree (reversalvoucher);
CREATE INDEX idx_egcl_dishonorcheque_status ON egcl_dishonorcheque USING btree (status);
CREATE INDEX idx_egcl_dishonorcheque_state ON egcl_dishonorcheque USING btree (state_id);


CREATE TABLE egcl_dishonorchequedetail (
    id bigint NOT NULL,
    dishonorcheque bigint NOT NULL,
    chartofaccounts bigint NOT NULL,
    debitamt double precision,
    creditamt double precision,
    function bigint,
    version numeric
);
    
CREATE SEQUENCE seq_egcl_dishonorchequedetail;
    
ALTER TABLE ONLY egcl_dishonorchequedetail
    ADD CONSTRAINT pk_egcl_dishonorchequedetail PRIMARY KEY (id);
ALTER TABLE ONLY egcl_dishonorchequedetail
    ADD CONSTRAINT fk_egcl_dishchqdet_dishonorchq FOREIGN KEY (dishonorcheque) REFERENCES egcl_dishonorcheque(id);
ALTER TABLE ONLY egcl_dishonorchequedetail
    ADD CONSTRAINT fk_egcl_dishchqdet_glcode FOREIGN KEY (chartofaccounts) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY egcl_dishonorchequedetail
    ADD CONSTRAINT fk_egcl_dishchqdet_function FOREIGN KEY (function) REFERENCES function(id);
    
CREATE INDEX idx_egcl_dishchqdet_dishonorcheque ON egcl_dishonorchequedetail USING btree (dishonorcheque);
CREATE INDEX idx_egcl_dishchqdet_chartofaccounts ON egcl_dishonorchequedetail USING btree (chartofaccounts);
CREATE INDEX idx_egcl_dishchqdet_function ON egcl_dishonorchequedetail USING btree (function);

CREATE TABLE egcl_dishonorchequesubdetail (
    id bigint NOT NULL,
    dishonorchequedetail bigint NOT NULL,
    amount double precision,
    detailkey bigint,
    detailtype bigint,
    version numeric
);
    
CREATE SEQUENCE seq_egcl_dishonorchequesubdetail;

ALTER TABLE ONLY egcl_dishonorchequesubdetail
    ADD CONSTRAINT pk_egcl_dishonorchequesubdetail PRIMARY KEY (id);
ALTER TABLE ONLY egcl_dishonorchequesubdetail
    ADD CONSTRAINT fk_egcl_dishchqsubdetail FOREIGN KEY (dishonorchequedetail) REFERENCES egcl_dishonorchequedetail(id);
ALTER TABLE ONLY egcl_dishonorchequesubdetail
    ADD CONSTRAINT fk_egcl_dishchqsubdet_acdet FOREIGN KEY (detailtype) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY egcl_dishonorchequesubdetail
    ADD CONSTRAINT fk_egcl_dishchqsubdet_acdetky FOREIGN KEY (detailkey) REFERENCES accountdetailkey(id);
    
CREATE INDEX idx_egcl_dishchqsubdet_dishonorchequedet ON egcl_dishonorchequesubdetail USING btree (dishonorchequedetail);
CREATE INDEX idx_egcl_dishchqsubdet_detailtype ON egcl_dishonorchequesubdetail USING btree (detailtype);
CREATE INDEX idx_egcl_dishchqsubdet_detailkey ON egcl_dishonorchequesubdetail USING btree (detailkey);


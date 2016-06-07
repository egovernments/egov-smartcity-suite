ALTER TABLE egf_dishonorcheque_detail DROP COLUMN detailkey RESTRICT;
ALTER TABLE egf_dishonorcheque_detail DROP COLUMN detailtype RESTRICT;

CREATE TABLE egf_dishonorcheque_sl_detail (
    id bigint NOT NULL,
    detailid bigint NOT NULL,
    amount bigint,
    detailkeyid bigint,
    detailtypeid bigint,
    version numeric
);
    
CREATE SEQUENCE seq_egf_dishonorcheque_sl_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

ALTER TABLE egf_dishonorcheque ADD COLUMN version numeric;

ALTER TABLE egf_dishonorcheque_detail ADD COLUMN version numeric;

ALTER TABLE egf_dishonorcheque RENAME COLUMN modifiedby TO lastmodifiedby;

ALTER TABLE egf_dishonorcheque RENAME COLUMN modifieddate TO lastmodifieddate;

ALTER TABLE egf_dishonorcheque RENAME COLUMN stateid TO state_id;

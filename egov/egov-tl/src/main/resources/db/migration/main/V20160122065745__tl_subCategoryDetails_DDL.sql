CREATE TABLE egtl_subcategory_details (
    id bigint NOT NULL,
    subcategory_id bigint NOT NULL,
    feetype_id bigint,
    uom_id bigint,
    ratetype character varying(50)
);

ALTER TABLE ONLY egtl_subcategory_details
    ADD CONSTRAINT pk_egtl_subcategory_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_subcategory_details
    ADD CONSTRAINT fk_scdetails_subcategory FOREIGN KEY (subcategory_id) REFERENCES egtl_mstr_sub_category(id);
ALTER TABLE ONLY egtl_subcategory_details
    ADD CONSTRAINT fk_scdetails_uom FOREIGN KEY (uom_id) REFERENCES egtl_mstr_unitofmeasure(id);
ALTER TABLE ONLY egtl_subcategory_details
    ADD CONSTRAINT fk_scdetails_feetype FOREIGN KEY (feetype_id) REFERENCES egtl_mstr_fee_type(id);
CREATE UNIQUE INDEX idx_egtl_subcategory_details ON egtl_subcategory_details USING btree (id);


CREATE SEQUENCE seq_egtl_subcategory_details;

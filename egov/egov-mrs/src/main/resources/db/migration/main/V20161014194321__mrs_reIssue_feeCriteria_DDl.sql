ALTER TABLE egmrs_reissue ALTER COLUMN feeCriteria type bigint using cast(feeCriteria as bigint);

ALTER TABLE ONLY egmrs_reissue
    ADD CONSTRAINT fk_reissue_feeCriteria FOREIGN KEY (feeCriteria) REFERENCES egmrs_fee(id);

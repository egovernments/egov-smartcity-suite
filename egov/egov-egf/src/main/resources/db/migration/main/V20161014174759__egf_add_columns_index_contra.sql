ALTER TABLE ONLY egf_instrumentotherdetails
    ADD CONSTRAINT fk_egf_instrumentotherdetails FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);

CREATE INDEX idx_instrumentheaderid ON egf_instrumentotherdetails USING btree (instrumentheaderid);

ALTER TABLE contrajournalvoucher
   ADD COLUMN createddate timestamp without time zone;

ALTER TABLE contrajournalvoucher
   ADD COLUMN lastmodifieddate timestamp without time zone;


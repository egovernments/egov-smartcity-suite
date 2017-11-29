
ALTER TABLE egswtax_connection RENAME column dhsc_number TO shsc_number;

DROP INDEX idx_swtax_connection_dhsc_number;

CREATE INDEX idx_swtax_connection_shsc_number ON egswtax_connection USING btree (shsc_number);



ALTER TABLE egswtax_applicationdetails ADD COLUMN parent bigint;

ALTER TABLE ONLY egswtax_applicationdetails
ADD CONSTRAINT fk_applicationdetails_parent FOREIGN KEY (parent) REFERENCES egswtax_applicationdetails (id);


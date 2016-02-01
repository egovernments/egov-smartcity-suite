ALTER TABLE egadtax_permitdetails ADD CONSTRAINT fk_adtax_permitdtl_status FOREIGN KEY (status) REFERENCES egw_status (id);

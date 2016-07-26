----advocate master table
ALTER TABLE eglc_advocate_master ALTER COLUMN ifsccode TYPE character varying(20);
ALTER TABLE eglc_advocate_master ALTER COLUMN tinumber TYPE character varying(20);
ALTER TABLE eglc_advocate_master ALTER COLUMN specilization TYPE character varying(20);
----advocate master auditing table
ALTER TABLE eglc_advocate_master_aud ALTER COLUMN ifsccode TYPE character varying(20);
ALTER TABLE eglc_advocate_master_aud ALTER COLUMN tinumber TYPE character varying(20);
ALTER TABLE eglc_advocate_master_aud ALTER COLUMN specilization TYPE character varying(20);
-----alter rename table name
ALTER TABLE eglc_documents RENAME TO eglc_legalcase_filestore;
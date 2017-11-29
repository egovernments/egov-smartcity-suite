-----adding user column in advaocate master table 
ALTER TABLE eglc_advocate_master  DROP column if exists advocateuser;
ALTER TABLE eglc_advocate_master  ADD column advocateuser bigint ;
ALTER TABLE eglc_advocate_master DROP CONSTRAINT if exists fk_advomaster_advocateuser;
ALTER TABLE eglc_advocate_master_aud  DROP column if exists advocateuser;
ALTER TABLE eglc_advocate_master_aud ADD column advocateuser bigint ; 


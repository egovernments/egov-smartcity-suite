-----adding user column in advaocate master table 

ALTER TABLE eglc_advocate_master  ADD column advocateuser bigint ;
alter table eglc_advocate_master add CONSTRAINT fk_advomaster_advocateuser FOREIGN KEY (advocateuser) REFERENCES eg_user (id);

ALTER TABLE eglc_advocate_master_aud ADD column advocateuser bigint ; 
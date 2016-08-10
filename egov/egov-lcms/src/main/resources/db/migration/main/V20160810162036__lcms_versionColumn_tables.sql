alter table eglc_counter_affidavit add column version bigint default 0 NOT NULL;

alter table eglc_pwr_document add column version bigint default 0 NOT NULL;
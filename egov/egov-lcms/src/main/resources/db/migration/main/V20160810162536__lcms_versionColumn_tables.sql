alter table eglc_legalcase_document drop column  if exists version ;

alter table eglc_legalcase_document add column version bigint default 0 NOT NULL;

alter table eglc_PWR drop column  if exists version ;

alter table eglc_PWR add column version bigint default 0 NOT NULL;
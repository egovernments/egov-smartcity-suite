alter table egswtax_notice  drop constraint uniq_noticeno;
alter table egswtax_notice  add constraint uniq_noticeno_noticetype unique(noticeno,noticetype);

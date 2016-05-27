create sequence seq_jv_billnumber_2016_17;

select setval('seq_jv_billnumber_2016_17', (select nextval('sq_billnumber_mn')-1));

drop sequence sq_billnumber_mn;

delete from eg_script where name = 'autobillnumber';
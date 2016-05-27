create sequence seq_jv_billnumber_2016_17;

select setval('seq_jv_billnumber_2016_17', ((Select case when seq = null then 2 else case when seq=0 then 2 else seq end end from (select nextval('sq_billnumber_mn') as seq) t )-1));

drop sequence sq_billnumber_mn;

delete from eg_script where name = 'autobillnumber';

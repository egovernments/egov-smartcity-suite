create if not exists sequence seq_jv_billnumber_2016_17;

create if not exists sq_billnumber_mn;

select setval('seq_jv_billnumber_2016_17', ((Select case when seq = null then 2 else case when seq=0 then 2 else seq end end from (select nextval('sq_billnumber_mn') as seq) t )-1));

drop if exists sequence sq_billnumber_mn;

delete from eg_script where name = 'autobillnumber';
